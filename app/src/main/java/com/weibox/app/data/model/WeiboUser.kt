package com.weibox.app.data.model

data class WeiboUser(
    val id: String,
    val screenName: String,
    val description: String,
    val avatarUrl: String,
    val coverUrl: String,
    val followersCount: String,
    val followCount: Int,
    val statusesCount: Int,
    val verified: Boolean = false,
    val verifiedReason: String = ""
)
