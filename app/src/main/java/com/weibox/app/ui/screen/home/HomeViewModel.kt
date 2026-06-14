package com.weibox.app.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weibox.app.data.model.WeiboPost
import com.weibox.app.data.prefs.AppPreferences
import com.weibox.app.data.repository.WeiboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REFRESH_INTERVAL_MS = 3 * 60 * 1000L  // 3 分钟

data class HomeUiState(
    val posts: List<WeiboPost> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val currentPage: Int = 1,
    val hasMore: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repo: WeiboRepository,
    private val prefs: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        repo.getCachedTimeline()
            .onEach { posts ->
                _state.update {
                    it.copy(posts = posts, isLoading = false, isEmpty = posts.isEmpty())
                }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val lastRefresh = prefs.lastRefreshTime.first()
            val elapsed = System.currentTimeMillis() - lastRefresh
            if (elapsed > REFRESH_INTERVAL_MS) {
                refresh()
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            runCatching { repo.refreshTimeline() }
                .onSuccess { posts ->
                    prefs.saveLastRefreshTime(System.currentTimeMillis())
                    _state.update { it.copy(currentPage = 1, hasMore = posts.isNotEmpty()) }
                }
                .onFailure { e -> _state.update { it.copy(error = e.message) } }
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoadingMore || s.isRefreshing || !s.hasMore) return
        viewModelScope.launch {
            val nextPage = s.currentPage + 1
            _state.update { it.copy(isLoadingMore = true) }
            runCatching { repo.loadMoreTimeline(nextPage) }
                .onSuccess { newPosts ->
                    _state.update {
                        it.copy(
                            currentPage = nextPage,
                            hasMore = newPosts.isNotEmpty(),
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure { _state.update { it.copy(isLoadingMore = false) } }
        }
    }
}
