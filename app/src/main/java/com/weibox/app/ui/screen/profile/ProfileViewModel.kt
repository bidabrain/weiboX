package com.weibox.app.ui.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weibox.app.data.model.WeiboPost
import com.weibox.app.data.model.WeiboUser
import com.weibox.app.data.repository.WeiboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: WeiboUser? = null,
    val posts: List<WeiboPost> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isFollowed: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repo: WeiboRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private var userId: String = ""

    fun init(uid: String) {
        if (userId == uid) return
        userId = uid
        repo.isFollowed(uid)
            .onEach { followed -> _state.update { it.copy(isFollowed = followed) } }
            .launchIn(viewModelScope)
        load()
    }

    private fun load() {
        viewModelScope.launch {
            Log.d("WeiboProfile", "load() userId=$userId")
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val user = repo.fetchUser(userId)
                Log.d("WeiboProfile", "fetchUser ok: ${user.screenName}")
                val posts = repo.refreshUserPosts(userId, 1)
                Log.d("WeiboProfile", "refreshUserPosts ok: ${posts.size} posts")
                _state.update { it.copy(user = user, posts = posts, isLoading = false, currentPage = 1) }
            }.onFailure { e ->
                Log.e("WeiboProfile", "load failed: ${e::class.simpleName} msg=${e.message}", e)
                _state.update { it.copy(error = e.message ?: e::class.simpleName, isLoading = false) }
            }
        }
    }

    fun loadMore() {
        if (_state.value.isRefreshing || !_state.value.hasMore) return
        viewModelScope.launch {
            val nextPage = _state.value.currentPage + 1
            _state.update { it.copy(isRefreshing = true) }
            runCatching { repo.refreshUserPosts(userId, nextPage) }
                .onSuccess { newPosts ->
                    _state.update {
                        it.copy(
                            posts = it.posts + newPosts,
                            isRefreshing = false,
                            currentPage = nextPage,
                            hasMore = newPosts.isNotEmpty()
                        )
                    }
                }
                .onFailure { _state.update { it.copy(isRefreshing = false) } }
        }
    }

    fun toggleFollow() {
        val user = _state.value.user ?: return
        viewModelScope.launch {
            if (_state.value.isFollowed) repo.unfollowUser(user.id)
            else repo.followUser(user)
        }
    }
}
