package com.weibox.app.data.api

import android.util.Log
import com.weibox.app.data.model.WeiboPost
import com.weibox.app.data.model.WeiboUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class CaptchaRequiredException(val captchaUrl: String) : RuntimeException("触发验证码限制")

private val DATE_FORMAT = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)
private val USER_AGENTS = listOf(
    "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Mobile/15E148 Safari/604.1",
    "Mozilla/5.0 (Linux; Android 14; SM-S9180) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.6367.113 Mobile Safari/537.36"
)

class WeiboApi(cookieString: String) {

    private val hasCookie = cookieString.isNotBlank()

    // 无 CookieJar 的简单客户端，专用于 s.weibo.com 访客搜索
    private val plainClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    // CookieJar：初始化时解析存储的 cookie，自动捕获服务器下发的新 token
    private val cookieJar = object : CookieJar {
        private val jar = mutableListOf<Cookie>()

        init {
            if (cookieString.isNotBlank()) {
                cookieString.split(";").forEach { part ->
                    val kv = part.trim().split("=", limit = 2)
                    if (kv.size == 2) {
                        runCatching {
                            jar.add(
                                Cookie.Builder()
                                    .name(kv[0].trim())
                                    .value(kv[1].trim())
                                    .domain("m.weibo.cn")
                                    .path("/")
                                    .build()
                            )
                        }
                    }
                }
            }
        }

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookies.forEach { new ->
                jar.removeAll { it.name == new.name }
                jar.add(new)
                Log.d("WeiboJar", "cookie updated: ${new.name}=${new.value.take(12)}...")
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> = jar.toList()

        fun getXsrfToken(): String = jar.find { it.name == "XSRF-TOKEN" }?.value ?: ""
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .addInterceptor(HttpLoggingInterceptor { Log.d("WeiboHttp", it) }.apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        })
        .addInterceptor { chain ->
            val xsrf = cookieJar.getXsrfToken()
            val req = chain.request().newBuilder()
                .header("User-Agent", USER_AGENTS.random())
                .header("Accept", "application/json, text/plain, */*")
                .header("Referer", "https://m.weibo.cn/")
                .header("MWeibo-Pwa", "1")
                .header("X-Requested-With", "XMLHttpRequest")
                .apply { if (xsrf.isNotEmpty()) header("X-XSRF-TOKEN", xsrf) }
                .build()
            chain.proceed(req)
        }
        .build()

    suspend fun getUserInfo(userId: String): WeiboUser = withContext(Dispatchers.IO) {
        val url = "https://m.weibo.cn/api/container/getIndex?containerid=100505$userId"
        val body = get(url)
        parseUserInfo(body)
    }

    suspend fun getUserPosts(userId: String, page: Int): List<WeiboPost> = withContext(Dispatchers.IO) {
        val url = "https://m.weibo.cn/api/container/getIndex?containerid=230413$userId&page=$page&count=20"
        val body = get(url)
        parsePosts(body)
    }

    // page 从 2 开始，第 1 页是分类推荐卡片
    suspend fun getFollowingList(userId: String, page: Int): List<WeiboUser> = withContext(Dispatchers.IO) {
        val url = "https://m.weibo.cn/api/container/getIndex" +
                "?containerid=231051_-_followers_-_$userId&page=$page"
        val body = get(url)
        val root = JSONObject(body)
        if (root.optInt("ok") != 1) return@withContext emptyList()
        val cards = root.optJSONObject("data")?.optJSONArray("cards") ?: return@withContext emptyList()
        val users = mutableListOf<WeiboUser>()
        for (i in 0 until cards.length()) {
            val group = cards.getJSONObject(i).optJSONArray("card_group") ?: continue
            for (j in 0 until group.length()) {
                val item = group.getJSONObject(j)
                if (item.optInt("card_type") == 10) {
                    item.optJSONObject("user")?.let { users.add(parseUserJson(it)) }
                }
            }
        }
        users
    }

    suspend fun getComments(postId: String, maxId: String? = null, page: Int = 1): Pair<List<com.weibox.app.data.model.WeiboComment>, String?> = withContext(Dispatchers.IO) {
        val body = if (cookieJar.getXsrfToken().isNotEmpty()) {
            val url = buildString {
                append("https://m.weibo.cn/comments/hotflow?max_id_type=0&mid=$postId")
                if (maxId != null) append("&max_id=$maxId")
            }
            get(url)
        } else {
            get("https://m.weibo.cn/api/comments/show?id=$postId&page=$page")
        }
        parseComments(body, hasCookie = cookieJar.getXsrfToken().isNotEmpty())
    }

    private fun parseComments(json: String, hasCookie: Boolean): Pair<List<com.weibox.app.data.model.WeiboComment>, String?> {
        val root = JSONObject(json)
        val data = root.optJSONObject("data") ?: return Pair(emptyList(), null)
        val arr = data.optJSONArray("data") ?: return Pair(emptyList(), null)
        val nextMaxId = if (hasCookie) data.optString("max_id").takeIf { it.isNotEmpty() && it != "0" } else null

        val comments = (0 until arr.length()).mapNotNull { i ->
            runCatching {
                val c = arr.getJSONObject(i)
                val user = c.optJSONObject("user")
                com.weibox.app.data.model.WeiboComment(
                    id = c.optString("id"),
                    text = stripHtml(c.optString("text")),
                    createdAt = c.optString("created_at"),
                    createdAtTimestamp = runCatching {
                        DATE_FORMAT.parse(c.optString("created_at"))?.time ?: 0L
                    }.getOrDefault(0L),
                    userName = user?.optString("screen_name") ?: "",
                    userAvatar = user?.optString("avatar_hd")?.ifEmpty { user.optString("profile_image_url") } ?: "",
                    likeCount = c.optInt("like_count")
                )
            }.getOrNull()
        }
        return Pair(comments, nextMaxId)
    }

    suspend fun searchUsers(query: String, page: Int = 1): List<WeiboUser> = withContext(Dispatchers.IO) {
        if (!hasCookie) {
            searchUsersByWeb(query, page)
        } else {
            val encoded = java.net.URLEncoder.encode(query, "UTF-8")
            val url = "https://m.weibo.cn/api/container/getIndex" +
                    "?containerid=100103type%3D3%26q%3D$encoded" +
                    "&page_type=searchall&page=$page"
            val body = get(url)
            parseUserSearch(body)
        }
    }

    // ── 访客模式搜索（无 Cookie）────────────────────────────────────

    private fun getVisitorTokens(): Pair<String, String> {
        val request = Request.Builder()
            .url("https://passport.weibo.com/visitor/genvisitor2?cb=visitor_gray_callback&reg=0")
            .header("User-Agent", USER_AGENTS.first())
            .header("Referer", "https://s.weibo.com/")
            .get()
            .build()
        val body = plainClient.newCall(request).execute().body?.string()
            ?: error("访客 token 获取失败")
        // 响应是 JSONP：visitor_gray_callback({...})
        val json = JSONObject(body.substringAfter("(").substringBeforeLast(")"))
        if (json.optInt("retcode") != 20000000) error("访客 token 返回异常")
        val data = json.getJSONObject("data")
        return Pair(data.getString("sub"), data.getString("subp"))
    }

    private fun searchUsersByWeb(query: String, page: Int = 1): List<WeiboUser> {
        val (sub, subp) = getVisitorTokens()
        val encoded = java.net.URLEncoder.encode(query, "UTF-8")
        val pageParam = if (page > 1) "&page=$page" else ""
        val request = Request.Builder()
            .url("https://s.weibo.com/user?q=$encoded&Refer=index$pageParam")
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
            .header("Referer", "https://weibo.com/")
            .header("Accept-Language", "zh-CN,zh;q=0.9")
            .header("Cookie", "SUB=$sub; SUBP=$subp")
            .get()
            .build()
        val html = plainClient.newCall(request).execute().body?.string()
            ?: return emptyList()
        return parseUserSearchHtml(html)
    }

    private fun parseUserSearchHtml(html: String): List<WeiboUser> {
        val doc = Jsoup.parse(html)
        return doc.select("div.card.card-user-b").mapNotNull { card ->
            runCatching {
                val href = card.selectFirst("div.avator a")?.attr("href") ?: return@mapNotNull null
                val uid = href.substringAfterLast("/")
                if (uid.isBlank() || !uid.all { it.isDigit() }) return@mapNotNull null

                val avatarUrl = card.selectFirst("div.avator img")?.attr("src") ?: ""
                val screenName = card.selectFirst("a.name")?.text() ?: ""
                val paras = card.select("div.info p")
                val description = paras.getOrNull(0)?.text() ?: ""
                val followersCount = card.selectFirst("span.s-nobr")?.text()
                    ?.removePrefix("粉丝：") ?: ""
                val verified = card.selectFirst("span.woo-icon-wrap") != null

                WeiboUser(
                    id = uid,
                    screenName = screenName,
                    description = description,
                    avatarUrl = avatarUrl,
                    coverUrl = "",
                    followersCount = followersCount,
                    followCount = 0,
                    statusesCount = 0,
                    verified = verified,
                    verifiedReason = ""
                )
            }.getOrNull()
        }
    }

    private fun parseUserSearch(json: String): List<WeiboUser> {
        val root = JSONObject(json)
        checkOk(root)
        val cards = root.optJSONObject("data")?.optJSONArray("cards") ?: return emptyList()
        val users = mutableListOf<WeiboUser>()
        for (i in 0 until cards.length()) {
            val card = cards.getJSONObject(i)
            card.optJSONObject("user")?.let { users.add(parseUserJson(it)) }
            card.optJSONArray("card_group")?.let { group ->
                for (j in 0 until group.length()) {
                    group.getJSONObject(j).optJSONObject("user")
                        ?.let { users.add(parseUserJson(it)) }
                }
            }
        }
        return users.distinctBy { it.id }
    }

    private fun parseUserJson(u: JSONObject) = WeiboUser(
        id = u.optString("id"),
        screenName = u.optString("screen_name"),
        description = u.optString("description"),
        avatarUrl = u.optString("avatar_hd").ifEmpty { u.optString("profile_image_url") },
        coverUrl = u.optString("cover_image_phone"),
        followersCount = u.optString("followers_count"),
        followCount = u.optInt("follow_count"),
        statusesCount = u.optInt("statuses_count"),
        verified = u.optBoolean("verified"),
        verifiedReason = u.optString("verified_reason")
    )

    private fun get(url: String): String {
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: error("Empty response from $url")
        if (!response.isSuccessful) {
            val reason = when (response.code) {
                432  -> "访问受限（432），请在设置中配置 Cookie"
                401  -> "需要登录，请在设置中配置 Cookie"
                403  -> "无访问权限，请检查 Cookie 是否有效"
                429  -> "请求过于频繁，请稍后再试"
                else -> "网络错误 ${response.code}"
            }
            error(reason)
        }
        return body
    }

    private fun checkOk(root: JSONObject) {
        val ok = root.optInt("ok")
        if (ok == 1) return
        val msg = root.optString("msg").ifBlank { null }
        val url = root.optString("url")
        val reason = when {
            url.contains("captcha") -> "触发验证码限制，请稍后重试或更新 Cookie"
            ok == -100             -> "请求被拦截，请稍后重试或更新 Cookie"
            msg != null            -> msg
            else                   -> "接口错误 ok=$ok"
        }
        error(reason)
    }

    private fun parseUserInfo(json: String): WeiboUser {
        val root = JSONObject(json)
        checkOk(root)
        val data = root.optJSONObject("data") ?: error("接口无数据")
        val info = data.optJSONObject("userInfo") ?: error("无用户信息")
        return WeiboUser(
            id = info.optString("id"),
            screenName = info.optString("screen_name"),
            description = info.optString("description"),
            avatarUrl = info.optString("avatar_hd").ifEmpty { info.optString("profile_image_url") },
            coverUrl = info.optString("cover_image_phone"),
            followersCount = info.optString("followers_count"),
            followCount = info.optInt("follow_count"),
            statusesCount = info.optInt("statuses_count"),
            verified = info.optBoolean("verified"),
            verifiedReason = info.optString("verified_reason")
        )
    }

    private fun parsePosts(json: String): List<WeiboPost> {
        val root = JSONObject(json)
        if (root.optInt("ok") != 1) {
            val url = root.optString("url")
            if (url.contains("captcha")) throw CaptchaRequiredException(url)
            Log.w("WeiboHttp", "parsePosts: ok=${root.optInt("ok")} url=$url")
            return emptyList()
        }
        val cards = root.optJSONObject("data")?.optJSONArray("cards") ?: return emptyList()
        return (0 until cards.length())
            .mapNotNull { i ->
                val card = cards.getJSONObject(i)
                if (card.optInt("card_type") == 9) {
                    card.optJSONObject("mblog")?.let { runCatching { parsePost(it) }.getOrNull() }
                } else null
            }
    }

    fun parsePost(mblog: JSONObject): WeiboPost {
        val user = mblog.optJSONObject("user")
        val pics = parsePics(mblog.optJSONArray("pics"))
        val retweeted = mblog.optJSONObject("retweeted_status")
        val createdAtStr = mblog.optString("created_at")
        val timestamp = runCatching { DATE_FORMAT.parse(createdAtStr)?.time ?: 0L }.getOrDefault(0L)
        val rawText = mblog.optString("text")

        return WeiboPost(
            id = mblog.optString("id"),
            userId = user?.optString("id") ?: "",
            userName = user?.optString("screen_name") ?: "",
            userAvatar = user?.optString("avatar_hd")?.ifEmpty { user.optString("profile_image_url") } ?: "",
            text = stripHtml(rawText),
            pics = pics,
            createdAt = createdAtStr,
            createdAtTimestamp = timestamp,
            likesCount = mblog.optInt("attitudes_count"),
            commentsCount = mblog.optInt("comments_count"),
            repostsCount = mblog.optInt("reposts_count"),
            source = stripHtml(mblog.optString("source")),
            isRetweet = retweeted != null,
            retweetPost = retweeted?.let { runCatching { parsePost(it) }.getOrNull() }
        )
    }

    private fun parsePics(arr: JSONArray?): List<String> {
        arr ?: return emptyList()
        return (0 until arr.length()).map { i ->
            val pic = arr.getJSONObject(i)
            pic.optJSONObject("large")?.optString("url") ?: pic.optString("url")
        }.filter { it.isNotEmpty() }
    }

    private fun stripHtml(html: String): String =
        android.text.Html.fromHtml(html, android.text.Html.FROM_HTML_MODE_COMPACT)
            .toString().trim()
}
