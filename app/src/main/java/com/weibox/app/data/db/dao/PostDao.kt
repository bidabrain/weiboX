package com.weibox.app.data.db.dao

import androidx.room.*
import com.weibox.app.data.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

data class UserLastPost(val userId: String, val lastPostAt: Long)
data class UserPostStats(val userId: String, val postCount: Int, val oldestPostAt: Long, val newestPostAt: Long)

@Dao
interface PostDao {
    @Query("SELECT * FROM cached_posts ORDER BY createdAtTimestamp DESC")
    fun getTimeline(): Flow<List<PostEntity>>

    @Query("SELECT * FROM cached_posts WHERE userId = :userId ORDER BY createdAtTimestamp DESC")
    fun getByUser(userId: String): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("DELETE FROM cached_posts WHERE userId = :userId")
    suspend fun deleteByUser(userId: String)

    @Query("DELETE FROM cached_posts WHERE cachedAt < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("SELECT COUNT(*) FROM cached_posts")
    suspend fun count(): Int

    @Query("SELECT userId, MAX(createdAtTimestamp) AS lastPostAt FROM cached_posts GROUP BY userId")
    suspend fun getLastPostTimestampByUser(): List<UserLastPost>

    @Query("""
        SELECT userId,
               COUNT(*) AS postCount,
               MIN(createdAtTimestamp) AS oldestPostAt,
               MAX(createdAtTimestamp) AS newestPostAt
        FROM cached_posts
        GROUP BY userId
    """)
    suspend fun getPostStatsByUser(): List<UserPostStats>

    @Query("""
        DELETE FROM cached_posts WHERE id IN (
            SELECT id FROM cached_posts ORDER BY createdAtTimestamp ASC LIMIT :n
        )
    """)
    suspend fun deleteOldest(n: Int)
}
