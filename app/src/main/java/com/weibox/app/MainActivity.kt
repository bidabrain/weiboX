package com.weibox.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.weibox.app.data.prefs.AppPreferences
import com.weibox.app.data.session.VisitorSession
import com.weibox.app.navigation.AppNavGraph
import com.weibox.app.ui.theme.WeiboXTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var prefs: AppPreferences
    @Inject lateinit var visitorSession: VisitorSession

    private var lastBackPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 注册在 setContent 之前，使 NavHost 的回调优先级更高（后注册先执行）
        // 只有在根页面回退栈为空时才触发此回调
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val now = System.currentTimeMillis()
                if (now - lastBackPressedTime < 2000L) {
                    finish()
                } else {
                    lastBackPressedTime = now
                    Toast.makeText(this@MainActivity, "再按一次退出应用", Toast.LENGTH_SHORT).show()
                }
            }
        })

        lifecycleScope.launch {
            if (prefs.cookie.first().isBlank()) {
                initVisitorSession()
            }
        }

        setContent {
            val darkMode by prefs.darkMode.collectAsState(initial = false)
            WeiboXTheme(darkTheme = darkMode) {
                AppNavGraph()
            }
        }
    }

    private fun initVisitorSession() {
        val cm = CookieManager.getInstance()
        cm.setAcceptCookie(true)

        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        cm.setAcceptThirdPartyCookies(webView, true)

        // 加入 window（1×1 不可见），确保 WebView 能正常渲染和执行 JS
        addContentView(webView, ViewGroup.LayoutParams(1, 1))

        val handler = Handler(Looper.getMainLooper())

        fun destroy() {
            handler.removeCallbacksAndMessages(null)
            (webView.parent as? ViewGroup)?.removeView(webView)
            webView.destroy()
        }

        var pollCount = 0

        fun poll() {
            cm.flush()
            val cookiesMWeibo   = cm.getCookie("https://m.weibo.cn") ?: ""
            val cookiesWeiboCn  = cm.getCookie("https://weibo.cn") ?: ""
            // 合并两个域，SUB 可能只在其中一个
            val allCookies = listOf(cookiesMWeibo, cookiesWeiboCn)
                .filter { it.isNotBlank() }.joinToString("; ")
            if (allCookies.contains("SUB=") && allCookies.contains("XSRF-TOKEN=")) {
                visitorSession.update(allCookies)
                Log.d("VisitorSession", "visitor session ready (poll=$pollCount)")
                destroy()
                return
            }
            pollCount++
            if (pollCount < 15) {
                handler.postDelayed(::poll, 1_000L)
            } else {
                Log.w("VisitorSession", "visitor session timeout")
                destroy()
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                Log.d("VisitorSession", "onPageFinished: $url")
                // 第一次落地 m.weibo.cn 后开始每秒轮询，等 JS 完成访客 session 初始化
                if (pollCount == 0 && "m.weibo.cn" in url) {
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed(::poll, 1_000L)
                }
            }
        }

        webView.loadUrl("https://m.weibo.cn")
    }
}
