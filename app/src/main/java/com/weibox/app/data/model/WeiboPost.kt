package com.weibox.app.data.model

data class WeiboPost(
    val id: String,
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
    val source: String = "",
    val isRetweet: Boolean = false,
    val retweetPost: WeiboPost? = null
)
