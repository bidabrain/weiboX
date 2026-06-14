package com.weibox.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weibox.app.data.model.WeiboUser

@Entity(tableName = "followed_users")
data class UserEntity(
    @PrimaryKey val id: String,
    val screenName: String,
    val description: String,
    val avatarUrl: String,
    val coverUrl: String,
    val followersCount: String,
    val followCount: Int,
    val statusesCount: Int,
    val verified: Boolean,
    val verifiedReason: String,
    val followedAt: Long = System.currentTimeMillis()
) {
    fun toModel() = WeiboUser(
        id = id,
        screenName = screenName,
        description = description,
        avatarUrl = avatarUrl,
        coverUrl = coverUrl,
        followersCount = followersCount,
        followCount = followCount,
        statusesCount = statusesCount,
        verified = verified,
        verifiedReason = verifiedReason
    )
}

fun WeiboUser.toEntity() = UserEntity(
    id = id,
    screenName = screenName,
    description = description,
    avatarUrl = avatarUrl,
    coverUrl = coverUrl,
    followersCount = followersCount,
    followCount = followCount,
    statusesCount = statusesCount,
    verified = verified,
    verifiedReason = verifiedReason
)
