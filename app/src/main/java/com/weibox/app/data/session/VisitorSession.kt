package com.weibox.app.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VisitorSession @Inject constructor() {
    private val _cookie = MutableStateFlow("")
    val cookie: StateFlow<String> = _cookie.asStateFlow()

    fun update(cookieString: String) {
        _cookie.value = cookieString
    }

    val isReady: Boolean get() = _cookie.value.isNotBlank()
}
