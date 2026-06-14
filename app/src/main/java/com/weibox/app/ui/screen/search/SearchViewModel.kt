package com.weibox.app.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weibox.app.data.model.WeiboUser
import com.weibox.app.data.repository.WeiboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<WeiboUser> = emptyList(),
    val followedIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val isLoadingMore: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: WeiboRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private var followObserverJobs = mutableMapOf<String, Job>()

    init {
        // 实时监听所有关注状态
        repo.getFollowedUsers()
            .onEach { users ->
                _state.update { it.copy(followedIds = users.map { u -> u.id }.toSet()) }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(q: String) {
        _state.update { it.copy(query = q, error = null) }
    }

    fun search() {
        val q = _state.value.query.trim()
        if (q.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, results = emptyList(), currentPage = 1, hasMore = true) }

            runCatching {
                if (q.all { it.isDigit() }) {
                    // 纯数字 → 直接按 user_id 查找
                    listOf(repo.fetchUser(q))
                } else {
                    // 关键词 → 搜索用户列表
                    repo.searchUsers(q, page = 1)
                }
            }.onSuccess { users ->
                _state.update {
                    it.copy(
                        results = users,
                        isLoading = false,
                        hasMore = users.size >= 10
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(error = "搜索失败：${e.message}", isLoading = false) }
            }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoadingMore || !s.hasMore || s.query.all { it.isDigit() }) return
        viewModelScope.launch {
            val nextPage = s.currentPage + 1
            _state.update { it.copy(isLoadingMore = true) }
            runCatching { repo.searchUsers(s.query.trim(), nextPage) }
                .onSuccess { more ->
                    _state.update {
                        it.copy(
                            results = it.results + more,
                            currentPage = nextPage,
                            hasMore = more.size >= 10,
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure { _state.update { it.copy(isLoadingMore = false) } }
        }
    }

    fun toggleFollow(user: WeiboUser) {
        viewModelScope.launch {
            if (_state.value.followedIds.contains(user.id)) {
                repo.unfollowUser(user.id)
            } else {
                repo.followUser(user)
            }
        }
    }
}
