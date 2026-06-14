package com.weibox.app.ui.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weibox.app.ui.components.UserCard
import com.weibox.app.ui.components.WeiboTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateToProfile: (String) -> Unit,
    vm: SearchViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            last >= listState.layoutInfo.totalItemsCount - 3
        }
    }
    LaunchedEffect(shouldLoadMore) { if (shouldLoadMore) vm.loadMore() }

    Scaffold(topBar = { WeiboTopBar("内容发现") }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                label = { Text("用户名或 ID") },
                placeholder = { Text("输入昵称关键词，或直接输入数字 ID") },
                trailingIcon = {
                    IconButton(onClick = vm::search) {
                        Icon(Icons.Filled.Search, contentDescription = "搜索")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { vm.search() })
            )
            HorizontalDivider()
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.error != null -> Box(
                    Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center
                ) { Text(state.error!!, color = MaterialTheme.colorScheme.error) }
                state.results.isEmpty() && state.query.isNotBlank() -> Box(
                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) { Text("没有找到相关用户", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) }
                state.results.isNotEmpty() -> LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(state.results, key = { it.id }) { user ->
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
                            Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
