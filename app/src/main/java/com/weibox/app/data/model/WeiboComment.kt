package com.weibox.app.data.model

data class WeiboComment(
    val id: String,
    val text: String,
    val createdAt: String,
    val createdAtTimestamp: Long,
    val userName: String,
    val userAvatar: String,
    val likeCount: Int
)
