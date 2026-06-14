package com.weibox.app.data.repository

import com.weibox.app.data.api.CaptchaRequiredException
import com.weibox.app.data.api.WeiboApi
import com.weibox.app.data.db.AppDatabase
import com.weibox.app.data.db.entity.toEntity
import com.weibox.app.data.model.WeiboPost
import com.weibox.app.data.model.WeiboUser
import com.weibox.app.data.prefs.AppPreferences
import com.weibox.app.data.session.CaptchaManager
import com.weibox.app.data.session.VisitorSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class WeiboRepository @Inject constructor(
    private val db: AppDatabase,
    private val prefs: AppPreferences,
    private val visitorSession: VisitorSession,
    private val captchaManager: CaptchaManager
) {
    private suspend fun effectiveCookie(): String {
        val userCookie = prefs.cookie.first()
        if (userCookie.isNotBlank()) return userCookie
        return withTimeoutOrNull(10_000L) {
            visitorSession.cookie.first { it.isNotBlank() }
        } ?: ""
    }

    private suspend fun api() = WeiboApi(effectiveCookie())

    // ── Following ────────────────────────────────────────────────
    fun getFollowedUsers(): Flow<List<WeiboUser>> =
        db.userDao().getAll().map { list -> list.map { it.toModel() } }

    fun isFollowed(userId: String): Flow<Boolean> = db.userDao().isFollowed(userId)

    suspend fun followUser(user: WeiboUser) = db.userDao().insert(user.toEntity())

    suspend fun unfollowUser(userId: String) {
        db.userDao().delete(userId)
        db.postDao().deleteByUser(userId)
    }

    // ── Search / User info ────────────────────────────────────────
    suspend fun fetchUser(userId: String): WeiboUser = api().getUserInfo(userId)

    suspend fun fetchFollowingList(userId: String, page: Int) =
        api().getFollowingList(userId, page)

    suspend fun fetchComments(postId: String, maxId: String? = null, page: Int = 1) =
        api().getComments(postId, maxId, page)

    suspend fun searchUsers(query: String, page: Int = 1): List<WeiboUser> =
        api().searchUsers(query, page)

    // ── Posts ─────────────────────────────────────────────────────
    fun getCachedTimeline(): Flow<List<WeiboPost>> =
        db.postDao().getTimeline().map { list -> list.map { it.toModel() } }

    fun getCachedUserPosts(userId: String): Flow<List<WeiboPost>> =
        db.postDao().getByUser(userId).map { list -> list.map { it.toModel() } }

    suspend fun refreshTimeline(): List<WeiboPost> =
        loadTimelinePage(page = 1)

    suspend fun loadMoreTimeline(page: Int): List<WeiboPost> =
        loadTimelinePage(page = page)

    private suspend fun loadTimelinePage(page: Int): List<WeiboPost> {
        val userIds = db.userDao().getAllIds()
        if (userIds.isEmpty()) return emptyList()

        // 按最近发帖时间排序：有缓存的活跃用户优先，无缓存的新关注用户排最后
        val lastPostMap = db.postDao().getLastPostTimestampByUser()
            .associate { it.userId to it.lastPostAt }
        val sortedIds = userIds.sortedByDescending { lastPostMap[it] ?: 0L }

        var api = api()
        val posts = mutableListOf<WeiboPost>()
        for ((index, uid) in sortedIds.withIndex()) {
            // 参考 weibo-crawler：请求间隔 3~6 秒随机，避免触发限流
            if (index > 0) delay(Random.nextLong(3_000L, 6_000L))

            var result = runCatching { api.getUserPosts(uid, page) }

            // captcha：挂起等待用户手动完成，完成后用新 cookie 重试一次
            if (result.exceptionOrNull() is CaptchaRequiredException) {
                val captchaUrl = (result.exceptionOrNull() as CaptchaRequiredException).captchaUrl
                captchaManager.await(captchaUrl)   // 挂起直到用户完成验证
                api = api()                         // 用新 cookie 重建实例
                result = runCatching { api.getUserPosts(uid, page) }
            }

            result.onSuccess { newPosts ->
                posts.addAll(newPosts)
                db.postDao().insertAll(newPosts.map { it.toEntity() })
            }
        }
        trimCache()
        return posts.sortedByDescending { it.createdAtTimestamp }
    }

    suspend fun refreshUserPosts(userId: String, page: Int = 1): List<WeiboPost> {
        val posts = api().getUserPosts(userId, page)
        db.postDao().insertAll(posts.map { it.toEntity() })
        return posts
    }

    private suspend fun trimCache() {
        db.postDao().deleteOlderThan(System.currentTimeMillis() - 7 * 24 * 3600 * 1000L)
        val count = db.postDao().count()
        if (count > MAX_CACHED_POSTS) {
            db.postDao().deleteOldest(count - MAX_CACHED_POSTS)
        }
    }

    companion object {
        private const val MAX_CACHED_POSTS = 500
    }
}
