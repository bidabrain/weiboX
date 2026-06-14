package com.weibox.app.ui.screen.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weibox.app.data.model.WeiboUser
import com.weibox.app.data.repository.WeiboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val repo: WeiboRepository
) : ViewModel() {

    val users: StateFlow<List<WeiboUser>> = repo.getFollowedUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun unfollow(userId: String) {
        viewModelScope.launch { repo.unfollowUser(userId) }
    }
}
