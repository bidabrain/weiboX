package com.weibox.app.data.session

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaptchaManager @Inject constructor() {

    private val _pendingUrl = MutableStateFlow<String?>(null)
    val pendingUrl: StateFlow<String?> = _pendingUrl.asStateFlow()

    private var deferred: CompletableDeferred<Unit>? = null

    /** 在后台协程里调用：展示验证码并挂起，直到用户完成 */
    suspend fun await(captchaUrl: String) {
        deferred = CompletableDeferred()
        _pendingUrl.value = captchaUrl
        deferred!!.await()
    }

    /** UI 完成验证后调用：唤醒挂起的协程 */
    fun resolve() {
        _pendingUrl.value = null
        deferred?.complete(Unit)
        deferred = null
    }
}
