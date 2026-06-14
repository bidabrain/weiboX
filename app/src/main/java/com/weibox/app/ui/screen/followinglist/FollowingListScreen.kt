package com.weibox.app.ui.screen.followinglist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weibox.app.ui.components.UserCard
import com.weibox.app.ui.components.WeiboTopBar

@Composable
fun FollowingListScreen(
    userId: String,
    userName: String,
    onBack: () -> Unit,
    onNavigateToProfile: (String) -> Unit,
    vm: FollowingListViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(userId) { vm.init(userId) }

    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            last >= listState.layoutInfo.totalItemsCount - 4
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) vm.loadMore()
    }

    Scaffold(
        topBar = {
            WeiboTopBar(
                title = "${userName}的关注",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> Box(
                Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> Box(
                Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
            }

            state.users.isEmpty() -> Box(
                Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("暂无数据", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }

            else -> LazyColumn(
                state = listState,
                modifier = Modifier.padding(padding).fillMaxSize()
            ) {
                items(state.users, key = { it.id }) { user ->
                    UserCard(
                        user = user,
                        isFollowed = state.followedIds.contains(user.id),
                        onClick = { onNavigateToProfile(user.id) },
                        onFollowToggle = { vm.toggleFollow(user) }
                    )
                    HorizontalDivider()
                }

                if (state.isLoadingMore) {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(modifier = Modifier.size(24.dp)) }
                    }
                }
            }
        }
    }
}
