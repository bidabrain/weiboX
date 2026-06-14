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
        loadTimelinePage(page = 1, maxUsers = 10, isBackground = false)

    suspend fun refreshTimelineBackground(): List<WeiboPost> =
        loadTimelinePage(page = 1, maxUsers = 20, isBackground = true)

    suspend fun loadMoreTimeline(page: Int): List<WeiboPost> =
        loadTimelinePage(page = page, maxUsers = 10, isBackground = false)

    private suspend fun loadTimelinePage(page: Int, maxUsers: Int, isBackground: Boolean): List<WeiboPost> {
        val users = db.userDao().getAllWithFetchInfo()
        if (users.isEmpty()) return emptyList()

        val now = System.currentTimeMillis()
        val postStats = db.postDao().getPostStatsByUser().associate { it.userId to it }

        // 计算每个用户的逾期率 = 距上次抓取时间 / 该用户平均发帖间隔
        // 跳过最近 MIN_CHECK_INTERVAL 内已抓过的用户
        val candidates = users.mapNotNull { user ->
            val timeSinceFetch = now - user.lastFetchedAt
            if (timeSinceFetch < MIN_CHECK_INTERVAL_MS) return@mapNotNull null

            val stats = postStats[user.id]
            val avgInterval = if (stats != null && stats.postCount > 1) {
                (stats.newestPostAt - stats.oldestPostAt) / (stats.postCount - 1)
            } else {
                DEFAULT_POST_INTERVAL_MS
            }.coerceAtLeast(MIN_POST_INTERVAL_MS)

            val priority = timeSinceFetch.toDouble() / avgInterval
            Pair(user.id, priority)
        }.sortedByDescending { it.second }.take(maxUsers)

        if (candidates.isEmpty()) return emptyList()

        var api = api()
        val posts = mutableListOf<WeiboPost>()
        for ((index, pair) in candidates.withIndex()) {
            val uid = pair.first
            if (index > 0) {
                if (isBackground) delay(Random.nextLong(6_000L, 12_000L))
                else delay(Random.nextLong(3_000L, 6_000L))
            }

            var result = runCatching { api.getUserPosts(uid, page) }

            if (result.exceptionOrNull() is CaptchaRequiredException) {
                if (isBackground) break  // 后台遇到验证码，终止本次同步
                val captchaUrl = (result.exceptionOrNull() as CaptchaRequiredException).captchaUrl
                captchaManager.await(captchaUrl)
                api = api()
                result = runCatching { api.getUserPosts(uid, page) }
            }

            result.onSuccess { newPosts ->
                posts.addAll(newPosts)
                db.postDao().insertAll(newPosts.map { it.toEntity() })
                db.userDao().updateLastFetchedAt(uid, now)
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
        private const val MIN_CHECK_INTERVAL_MS = 5 * 60 * 1000L       // 5 分钟内抓过则跳过
        private const val DEFAULT_POST_INTERVAL_MS = 6 * 60 * 60 * 1000L // 无历史时默认 6 小时
        private const val MIN_POST_INTERVAL_MS = 30 * 60 * 1000L        // 平均间隔最低 30 分钟
    }
}
