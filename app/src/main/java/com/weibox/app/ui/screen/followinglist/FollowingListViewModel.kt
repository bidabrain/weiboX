package com.weibox.app.ui.screen.followinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weibox.app.data.model.WeiboUser
import com.weibox.app.data.repository.WeiboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FollowingListUiState(
    val users: List<WeiboUser> = emptyList(),
    val followedIds: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true,
    val currentPage: Int = 2   // API 第 1 页是推荐卡片，从第 2 页起是真实列表
)

@HiltViewModel
class FollowingListViewModel @Inject constructor(
    private val repo: WeiboRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FollowingListUiState())
    val state: StateFlow<FollowingListUiState> = _state.asStateFlow()

    private var userId = ""

    init {
        repo.getFollowedUsers()
            .onEach { list -> _state.update { it.copy(followedIds = list.map { u -> u.id }.toSet()) } }
            .launchIn(viewModelScope)
    }

    fun init(uid: String) {
        if (userId == uid) return
        userId = uid
        load(2)
    }

    private fun load(page: Int) {
        viewModelScope.launch {
            if (page == 2) _state.update { it.copy(isLoading = true, error = null) }
            else _state.update { it.copy(isLoadingMore = true) }

            runCatching { repo.fetchFollowingList(userId, page) }
                .onSuccess { newUsers ->
                    _state.update {
                        it.copy(
                            users = if (page == 2) newUsers else it.users + newUsers,
                            isLoading = false,
                            isLoadingMore = false,
                            currentPage = page,
                            hasMore = newUsers.isNotEmpty()
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, isLoadingMore = false, error = e.message) }
                }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoadingMore || !s.hasMore) return
        load(s.currentPage + 1)
    }

    fun toggleFollow(user: WeiboUser) {
        viewModelScope.launch {
            if (_state.value.followedIds.contains(user.id)) repo.unfollowUser(user.id)
            else repo.followUser(user)
        }
    }
}
