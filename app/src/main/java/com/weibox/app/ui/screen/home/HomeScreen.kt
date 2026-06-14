package com.weibox.app.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weibox.app.ui.components.PostCard
import com.weibox.app.ui.components.WeiboTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: (String) -> Unit,
    scrollToTopTrigger: Int = 0,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val pullState = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    // 点击底部时间线按钮时滚到顶
    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            listState.animateScrollToItem(0)
        }
    }

    if (pullState.isRefreshing) {
        LaunchedEffect(Unit) { vm.refresh() }
    }
    LaunchedEffect(state.isRefreshing) {
        if (!state.isRefreshing) pullState.endRefresh()
    }

    // 滑到底部时加载更多
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
        topBar = { WeiboTopBar("时间线") }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .nestedScroll(pullState.nestedScrollConnection)
        ) {
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.isEmpty -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                    // 底部加载指示器
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }

            PullToRefreshContainer(
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            state.error?.let {
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                ) { Text(it) }
            }
        }
    }
}
