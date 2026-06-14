package com.weibox.app.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weibox.app.ui.components.PostCard
import com.weibox.app.ui.components.WeiboTopBar

@Composable
fun HomeScreen(
    onNavigateToProfile: (String) -> Unit,
    scrollToTopTrigger: Int = 0,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) listState.animateScrollToItem(0)
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            last >= listState.layoutInfo.totalItemsCount - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !state.isLoadingMore && !state.isRefreshing) vm.loadMore()
    }

    Scaffold(
        topBar = {
            WeiboTopBar(
                title = "时间线",
                actions = {
                    RefreshStatusBar(
                        isRefreshing = state.isRefreshing,
                        lastRefreshTime = state.lastRefreshTime,
                        onClick = { if (!state.isRefreshing) vm.refresh() }
                    )
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                state.isEmpty -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("还没有关注任何用户", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "去搜索页面添加关注",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                else -> LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(state.posts, key = { it.id }) { post ->
                        PostCard(post = post, onUserClick = onNavigateToProfile, repo = vm.repo)
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

            state.error?.let {
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                ) { Text(it) }
            }
        }
    }
}

@Composable
private fun RefreshStatusBar(
    isRefreshing: Boolean,
    lastRefreshTime: Long,
    onClick: () -> Unit
) {
    val label = when {
        isRefreshing  -> "正在刷新"
        lastRefreshTime == 0L -> "点击刷新"
        else -> timeAgo(lastRefreshTime)
    }
    val color = if (isRefreshing)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    Row(
        Modifier
            .clickable(enabled = !isRefreshing, onClick = onClick)
            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier.size(11.dp),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(5.dp))
        }
        Text(label, style = MaterialTheme.typography.labelSmall, color = color)
    }
}

private fun timeAgo(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    return when {
        diff < 60_000L       -> "刚刚更新"
        diff < 3600_000L     -> "${diff / 60_000} 分钟前"
        diff < 86400_000L    -> "${diff / 3600_000} 小时前"
        else                 -> "${diff / 86400_000} 天前"
    }
}
