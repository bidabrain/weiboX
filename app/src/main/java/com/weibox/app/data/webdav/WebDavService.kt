package com.weibox.app.data.webdav

import com.weibox.app.data.model.WeiboUser
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val BACKUP_FILENAME = "weibox_backup.json"

class WebDavService(
    private val baseUrl: String,
    private val username: String,
    private val password: String
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val credential = Credentials.basic(username, password)
    private val backupUrl  = "$baseUrl/$BACKUP_FILENAME"

    /** 把关注列表和 Cookie 序列化为 JSON 并 PUT 到 WebDAV */
    fun backup(users: List<WeiboUser>, cookie: String): Result<Unit> = runCatching {
        val json = buildBackupJson(users, cookie)
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val req = Request.Builder()
            .url(backupUrl)
            .header("Authorization", credential)
            .put(body)
            .build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) error("WebDAV 上传失败 (HTTP ${resp.code}): ${resp.message}")
    }

    /** 从 WebDAV GET 备份文件，解析为用户列表 + cookie */
    fun restore(): Result<Pair<List<WeiboUser>, String>> = runCatching {
        val req = Request.Builder()
            .url(backupUrl)
            .header("Authorization", credential)
            .get()
            .build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) error("WebDAV 下载失败 (HTTP ${resp.code}): ${resp.message}")
        val body = resp.body?.string() ?: error("响应体为空")
        parseBackupJson(body)
    }

    private fun buildBackupJson(users: List<WeiboUser>, cookie: String): String {
        val arr = JSONArray()
        users.forEach { u ->
            arr.put(JSONObject().apply {
                put("id",              u.id)
                put("screen_name",     u.screenName)
                put("description",     u.description)
                put("avatar_url",      u.avatarUrl)
                put("cover_url",       u.coverUrl)
                put("followers_count", u.followersCount)
                put("follow_count",    u.followCount)
                put("statuses_count",  u.statusesCount)
                put("verified",        u.verified)
                put("verified_reason", u.verifiedReason)
            })
        }
        return JSONObject().apply {
            put("version",     2)
            put("backup_time", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date()))
            put("cookie",      cookie)
            put("users",       arr)
        }.toString(2)
    }

    // 返回 Pair<用户列表, cookie>
    private fun parseBackupJson(json: String): Pair<List<WeiboUser>, String> {
        val root   = JSONObject(json)
        val cookie = root.optString("cookie")
        val arr    = root.getJSONArray("users")
        val users  = (0 until arr.length()).map { i ->
            val u = arr.getJSONObject(i)
            WeiboUser(
                id             = u.optString("id"),
                screenName     = u.optString("screen_name"),
                description    = u.optString("description"),
                avatarUrl      = u.optString("avatar_url"),
                coverUrl       = u.optString("cover_url"),
                followersCount = u.optString("followers_count"),
                followCount    = u.optInt("follow_count"),
                statusesCount  = u.optInt("statuses_count"),
                verified       = u.optBoolean("verified"),
                verifiedReason = u.optString("verified_reason")
            )
        }
        return Pair(users, cookie)
    }
}
