package com.weibox.app.data.repository

import com.weibox.app.data.api.WeiboApi
import com.weibox.app.data.db.AppDatabase
import com.weibox.app.data.db.entity.toEntity
import com.weibox.app.data.model.WeiboPost
import com.weibox.app.data.model.WeiboUser
import com.weibox.app.data.prefs.AppPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeiboRepository @Inject constructor(
    private val db: AppDatabase,
    private val prefs: AppPreferences
) {
    private fun api(cookie: String) = WeiboApi(cookie)

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
    suspend fun fetchUser(userId: String): WeiboUser {
        val cookie = prefs.cookie.first()
        return api(cookie).getUserInfo(userId)
    }

    suspend fun fetchFollowingList(userId: String, page: Int) =
        api(prefs.cookie.first()).getFollowingList(userId, page)

    suspend fun fetchComments(postId: String, maxId: String? = null, page: Int = 1) =
        api(prefs.cookie.first()).getComments(postId, maxId, page)

    suspend fun searchUsers(query: String, page: Int = 1): List<WeiboUser> {
        val cookie = prefs.cookie.first()
        return api(cookie).searchUsers(query, page)
    }

    // ── Posts ─────────────────────────────────────────────────────
    fun getCachedTimeline(): Flow<List<WeiboPost>> =
        db.postDao().getTimeline().map { list -> list.map { it.toModel() } }

    fun getCachedUserPosts(userId: String): Flow<List<WeiboPost>> =
        db.postDao().getByUser(userId).map { list -> list.map { it.toModel() } }

    suspend fun refreshTimeline(): List<WeiboPost> =
        loadTimelinePage(page = 1, replace = true)

    suspend fun loadMoreTimeline(page: Int): List<WeiboPost> =
        loadTimelinePage(page = page, replace = false)

    private suspend fun loadTimelinePage(page: Int, replace: Boolean): List<WeiboPost> {
        val cookie = prefs.cookie.first()
        val userIds = db.userDao().getAllIds()
        if (userIds.isEmpty()) return emptyList()

        // 顺序请求 + 间隔，避免触发 Weibo 限流/验证码
        val posts = mutableListOf<WeiboPost>()
        userIds.forEachIndexed { index, uid ->
            if (index > 0) delay(800L)
            runCatching { api(cookie).getUserPosts(uid, page) }
                .onSuccess { posts.addAll(it) }
        }
        val sorted = posts.sortedByDescending { it.createdAtTimestamp }

        db.postDao().insertAll(sorted.map { it.toEntity() })
        trimCache()
        return sorted
    }

    private suspend fun trimCache() {
        // 删除 7 天前的旧数据
        db.postDao().deleteOlderThan(System.currentTimeMillis() - 7 * 24 * 3600 * 1000L)
        // 总条数超过 500 时，删除最旧的那批
        val count = db.postDao().count()
        if (count > MAX_CACHED_POSTS) {
            db.postDao().deleteOldest(count - MAX_CACHED_POSTS)
        }
    }

    companion object {
        private const val MAX_CACHED_POSTS = 500
    }

    suspend fun refreshUserPosts(userId: String, page: Int = 1): List<WeiboPost> {
        val cookie = prefs.cookie.first()
        val posts = api(cookie).getUserPosts(userId, page)
        db.postDao().insertAll(posts.map { it.toEntity() })
        return posts
    }
}
