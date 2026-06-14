package com.weibox.app.data.db.dao

import androidx.room.*
import com.weibox.app.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM followed_users ORDER BY followedAt DESC")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT id FROM followed_users")
    suspend fun getAllIds(): List<String>

    @Query("SELECT * FROM followed_users WHERE id = :userId")
    suspend fun getById(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM followed_users WHERE id = :userId")
    suspend fun delete(userId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM followed_users WHERE id = :userId)")
    fun isFollowed(userId: String): Flow<Boolean>
}
