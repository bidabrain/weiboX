package com.weibox.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.weibox.app.data.model.WeiboPost
import org.json.JSONArray

class StringListConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = JSONArray(list).toString()

    @TypeConverter
    fun toList(json: String): List<String> {
        val arr = JSONArray(json)
        return (0 until arr.length()).map { arr.getString(it) }
    }
}

@Entity(tableName = "cached_posts")
@TypeConverters(StringListConverter::class)
data class PostEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val text: String,
    val pics: List<String>,
    val createdAt: String,
    val createdAtTimestamp: Long,
    val likesCount: Int,
    val commentsCount: Int,
    val repostsCount: Int,
    val source: String,
    val isRetweet: Boolean,
    val retweetUserId: String = "",
    val retweetUserName: String = "",
    val retweetText: String = "",
    val retweetPics: List<String> = emptyList(),
    val cachedAt: Long = System.currentTimeMillis()
) {
    fun toModel(): WeiboPost {
        val retweet = if (isRetweet && retweetUserId.isNotEmpty()) {
            WeiboPost(
                id = "${id}_rt",
                userId = retweetUserId,
                userName = retweetUserName,
                userAvatar = "",
                text = retweetText,
                pics = retweetPics,
                createdAt = "",
                createdAtTimestamp = 0L,
                likesCount = 0,
                commentsCount = 0,
                repostsCount = 0
            )
        } else null

        return WeiboPost(
            id = id,
            userId = userId,
            userName = userName,
            userAvatar = userAvatar,
            text = text,
            pics = pics,
            createdAt = createdAt,
            createdAtTimestamp = createdAtTimestamp,
            likesCount = likesCount,
            commentsCount = commentsCount,
            repostsCount = repostsCount,
            source = source,
            isRetweet = isRetweet,
            retweetPost = retweet
        )
    }
}

fun WeiboPost.toEntity() = PostEntity(
    id = id,
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    text = text,
    pics = pics,
    createdAt = createdAt,
    createdAtTimestamp = createdAtTimestamp,
    likesCount = likesCount,
    commentsCount = commentsCount,
    repostsCount = repostsCount,
    source = source,
    isRetweet = isRetweet,
    retweetUserId = retweetPost?.userId ?: "",
    retweetUserName = retweetPost?.userName ?: "",
    retweetText = retweetPost?.text ?: "",
    retweetPics = retweetPost?.pics ?: emptyList()
)
