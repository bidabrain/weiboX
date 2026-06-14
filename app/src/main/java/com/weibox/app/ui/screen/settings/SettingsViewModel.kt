package com.weibox.app.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weibox.app.data.prefs.AppPreferences
import com.weibox.app.data.repository.WeiboRepository
import com.weibox.app.data.webdav.WebDavService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val webDavMessage: String? = null   // 成功/失败提示
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: AppPreferences,
    private val repo: WeiboRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        prefs.cookie.onEach  { c -> _state.update { it.copy(cookie = c, cookieInput = c) } }.launchIn(viewModelScope)
        prefs.darkMode.onEach { d -> _state.update { it.copy(darkMode = d) } }.launchIn(viewModelScope)
        prefs.webDavUrl.onEach  { v -> _state.update { it.copy(webDavUrl = v, webDavUrlInput = v) } }.launchIn(viewModelScope)
        prefs.webDavUser.onEach { v -> _state.update { it.copy(webDavUser = v, webDavUserInput = v) } }.launchIn(viewModelScope)
        prefs.webDavPass.onEach { v -> _state.update { it.copy(webDavPass = v, webDavPassInput = v) } }.launchIn(viewModelScope)
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
