package com.weibox.app.ui.screen.settings

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weibox.app.R
import com.weibox.app.data.prefs.AppPreferences
import com.weibox.app.data.repository.WeiboRepository
import com.weibox.app.data.webdav.WebDavService
import com.weibox.app.worker.BackgroundRefreshWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

enum class WebDavOp { NONE, BACKING_UP, RESTORING }

data class SettingsUiState(
    val cookie: String = "",
    val cookieInput: String = "",
    val darkMode: Boolean = false,
    val saved: Boolean = false,
    // WebDAV
    val webDavUrl: String = "",
    val webDavUser: String = "",
    val webDavPass: String = "",
    val webDavUrlInput: String = "",
    val webDavUserInput: String = "",
    val webDavPassInput: String = "",
    val webDavConfigSaved: Boolean = false,
    val webDavOp: WebDavOp = WebDavOp.NONE,
    val webDavMessage: String? = null,
    val donateMessage: String? = null,
    val backgroundRefreshEnabled: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: AppPreferences,
    private val repo: WeiboRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        prefs.cookie.onEach  { c -> _state.update { it.copy(cookie = c, cookieInput = c) } }.launchIn(viewModelScope)
        prefs.darkMode.onEach { d -> _state.update { it.copy(darkMode = d) } }.launchIn(viewModelScope)
        prefs.webDavUrl.onEach  { v -> _state.update { it.copy(webDavUrl = v, webDavUrlInput = v) } }.launchIn(viewModelScope)
        prefs.webDavUser.onEach { v -> _state.update { it.copy(webDavUser = v, webDavUserInput = v) } }.launchIn(viewModelScope)
        prefs.webDavPass.onEach { v -> _state.update { it.copy(webDavPass = v, webDavPassInput = v) } }.launchIn(viewModelScope)
        prefs.backgroundRefreshEnabled.onEach { v -> _state.update { it.copy(backgroundRefreshEnabled = v) } }.launchIn(viewModelScope)
    }

    // ── Cookie ───────────────────────────────────────────────────
    fun onCookieInputChange(v: String) = _state.update { it.copy(cookieInput = v, saved = false) }

    fun saveCookie() = viewModelScope.launch {
        prefs.saveCookie(_state.value.cookieInput.trim())
        _state.update { it.copy(saved = true) }
    }

    fun clearCookie() = viewModelScope.launch {
        prefs.saveCookie("")
        _state.update { it.copy(cookieInput = "", saved = false) }
    }

    fun toggleDarkMode() = viewModelScope.launch { prefs.setDarkMode(!_state.value.darkMode) }

    fun toggleBackgroundRefresh() = viewModelScope.launch {
        val newValue = !_state.value.backgroundRefreshEnabled
        prefs.setBackgroundRefreshEnabled(newValue)
        if (!newValue) BackgroundRefreshWorker.cancel(context)
    }

    // ── WebDAV 配置 ──────────────────────────────────────────────
    fun onWebDavUrlChange(v: String)  = _state.update { it.copy(webDavUrlInput = v, webDavConfigSaved = false) }
    fun onWebDavUserChange(v: String) = _state.update { it.copy(webDavUserInput = v, webDavConfigSaved = false) }
    fun onWebDavPassChange(v: String) = _state.update { it.copy(webDavPassInput = v, webDavConfigSaved = false) }

    fun saveWebDavConfig() = viewModelScope.launch {
        val s = _state.value
        prefs.saveWebDav(s.webDavUrlInput.trim(), s.webDavUserInput.trim(), s.webDavPassInput)
        _state.update { it.copy(webDavConfigSaved = true, webDavMessage = null) }
    }

    // ── 备份 ─────────────────────────────────────────────────────
    fun backup() = viewModelScope.launch {
        val s = _state.value
        if (s.webDavUrl.isBlank()) {
            _state.update { it.copy(webDavMessage = "请先保存 WebDAV 配置") }
            return@launch
        }
        _state.update { it.copy(webDavOp = WebDavOp.BACKING_UP, webDavMessage = null) }

        val users  = repo.getFollowedUsers().first()
        val cookie = prefs.cookie.first()
        if (users.isEmpty() && cookie.isBlank()) {
            _state.update { it.copy(webDavOp = WebDavOp.NONE, webDavMessage = "关注列表和 Cookie 均为空，无需备份") }
            return@launch
        }

        val result = withContext(Dispatchers.IO) {
            WebDavService(s.webDavUrl, s.webDavUser, s.webDavPass).backup(users, cookie)
        }
        _state.update {
            it.copy(
                webDavOp = WebDavOp.NONE,
                webDavMessage = result.fold(
                    onSuccess = {
                        "备份成功：${users.size} 位用户" +
                        if (cookie.isNotBlank()) " + Cookie" else ""  + " 已上传"
                    },
                    onFailure = { e -> "备份失败：${e.message}" }
                )
            )
        }
    }

    // ── 支持开发者 ────────────────────────────────────────────────
    fun savePayQrCode() = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.payme)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "payme_qr.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                ) ?: error("无法创建图片")
                context.contentResolver.openOutputStream(uri)?.use {
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, it)
                }
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                dir.mkdirs()
                val file = File(dir, "payme_qr.jpg")
                FileOutputStream(file).use {
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, it)
                }
                MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)
            }
        }.fold(
            onSuccess = { _state.update { it.copy(donateMessage = "二维码已保存到相册") } },
            onFailure = { e -> _state.update { it.copy(donateMessage = "保存失败：${e.message}") } }
        )
    }

    // ── 恢复 ─────────────────────────────────────────────────────
    fun restore() = viewModelScope.launch {
        val s = _state.value
        if (s.webDavUrl.isBlank()) {
            _state.update { it.copy(webDavMessage = "请先保存 WebDAV 配置") }
            return@launch
        }
        _state.update { it.copy(webDavOp = WebDavOp.RESTORING, webDavMessage = null) }

        val result = withContext(Dispatchers.IO) {
            WebDavService(s.webDavUrl, s.webDavUser, s.webDavPass).restore()
        }

        result.fold(
            onSuccess = { (users, cookie) ->
                users.forEach { repo.followUser(it) }
                if (cookie.isNotBlank()) prefs.saveCookie(cookie)
                _state.update {
                    it.copy(
                        webDavOp = WebDavOp.NONE,
                        webDavMessage = "恢复成功：已导入 ${users.size} 位用户" +
                                if (cookie.isNotBlank()) " + Cookie" else ""
                    )
                }
            },
            onFailure = { e ->
                _state.update {
                    it.copy(webDavOp = WebDavOp.NONE, webDavMessage = "恢复失败：${e.message}")
                }
            }
        )
    }
}
