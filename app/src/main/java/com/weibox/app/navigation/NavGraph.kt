package com.weibox.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.weibox.app.ui.screen.following.FollowingScreen
import com.weibox.app.ui.screen.followinglist.FollowingListScreen
import com.weibox.app.ui.screen.home.HomeScreen
import com.weibox.app.ui.screen.profile.ProfileScreen
import com.weibox.app.ui.screen.search.SearchScreen
import com.weibox.app.ui.screen.settings.SettingsScreen

private sealed class Tab(val route: String, val label: String, val icon: ImageVector) {
    object Home      : Tab("home",      "时间线",   Icons.Filled.Home)
    object Search    : Tab("search",    "内容发现", Icons.Filled.Search)
    object Following : Tab("following", "关注",     Icons.Filled.People)
    object Settings  : Tab("settings",  "设置",     Icons.Filled.Settings)
}

private val tabs = listOf(Tab.Home, Tab.Search, Tab.Following, Tab.Settings)

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDest = backStack?.destination
    val showBottomBar = tabs.any { it.route == currentDest?.route }

    var homeScrollToTopTrigger by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    tabs.forEach { tab ->
                        val selected = currentDest?.hierarchy?.any { it.route == tab.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (selected && tab is Tab.Home) {
                                    homeScrollToTopTrigger++
                                } else {
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Tab.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Tab.Home.route) {
                HomeScreen(
                    onNavigateToProfile = { uid -> navController.navigate("profile/$uid") },
                    scrollToTopTrigger = homeScrollToTopTrigger
                )
            }
            composable(Tab.Search.route) {
                SearchScreen(onNavigateToProfile = { uid -> navController.navigate("profile/$uid") })
            }
            composable(Tab.Following.route) {
                FollowingScreen(onNavigateToProfile = { uid -> navController.navigate("profile/$uid") })
            }
            composable(Tab.Settings.route) { SettingsScreen() }
            composable(
                "profile/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { back ->
                val uid = back.arguments?.getString("userId") ?: return@composable
                ProfileScreen(
                    userId = uid,
                    onBack = { navController.popBackStack() },
                    onNavigateToFollowingList = { id, name ->
                        navController.navigate(
                            "followinglist/$id/${java.net.URLEncoder.encode(name, "UTF-8")}"
                        )
                    }
                )
            }
            composable(
                "followinglist/{userId}/{userName}",
                arguments = listOf(
                    navArgument("userId") { type = NavType.StringType },
                    navArgument("userName") { type = NavType.StringType }
                )
            ) { back ->
                val uid  = back.arguments?.getString("userId") ?: return@composable
                val name = back.arguments?.getString("userName")?.let {
                    java.net.URLDecoder.decode(it, "UTF-8")
                } ?: ""
                FollowingListScreen(
                    userId = uid,
                    userName = name,
                    onBack = { navController.popBackStack() },
                    onNavigateToProfile = { id -> navController.navigate("profile/$id") }
                )
            }
        }
    }
}
