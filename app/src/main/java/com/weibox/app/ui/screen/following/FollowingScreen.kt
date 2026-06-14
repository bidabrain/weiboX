package com.weibox.app.ui.screen.following

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.weibox.app.ui.components.UserCard
import com.weibox.app.ui.components.WeiboTopBar

@Composable
fun FollowingScreen(
    onNavigateToProfile: (String) -> Unit,
    vm: FollowingViewModel = hiltViewModel()
) {
    val users by vm.users.collectAsState()

    Scaffold(topBar = { WeiboTopBar("关注") }) { padding ->
        if (users.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("还没有关注任何用户", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        } else {
            LazyColumn(Modifier.padding(padding).fillMaxSize()) {
                items(users, key = { it.id }) { user ->
                    UserCard(
                        user = user,
                        isFollowed = true,
                        onClick = { onNavigateToProfile(user.id) },
                        onFollowToggle = { vm.unfollow(user.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
