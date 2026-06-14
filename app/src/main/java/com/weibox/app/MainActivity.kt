package com.weibox.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.weibox.app.data.prefs.AppPreferences
import com.weibox.app.navigation.AppNavGraph
import com.weibox.app.ui.theme.WeiboXTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkMode by prefs.darkMode.collectAsState(initial = false)
            WeiboXTheme(darkTheme = darkMode) {
                AppNavGraph()
            }
        }
    }
}
