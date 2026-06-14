package com.weibox.app.data.db.dao

import androidx.room.*
import com.weibox.app.data.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("""
        DELETE FROM cached_posts WHERE id IN (
            SELECT id FROM cached_posts ORDER BY createdAtTimestamp ASC LIMIT :n
        )
    """)
    suspend fun deleteOldest(n: Int)
}
