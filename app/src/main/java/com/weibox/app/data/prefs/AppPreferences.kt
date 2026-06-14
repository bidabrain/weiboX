package com.weibox.app.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("weibox_prefs")

private val KEY_COOKIE        = stringPreferencesKey("cookie")
private val KEY_DARK_MODE     = booleanPreferencesKey("dark_mode")
private val KEY_LAST_REFRESH  = longPreferencesKey("last_refresh")
private val KEY_WEBDAV_URL    = stringPreferencesKey("webdav_url")
private val KEY_WEBDAV_USER   = stringPreferencesKey("webdav_user")
private val KEY_WEBDAV_PASS   = stringPreferencesKey("webdav_pass")

class AppPreferences(private val context: Context) {

    val cookie: Flow<String>          = context.dataStore.data.map { it[KEY_COOKIE] ?: "" }
    val darkMode: Flow<Boolean>       = context.dataStore.data.map { it[KEY_DARK_MODE] ?: false }
    val lastRefreshTime: Flow<Long>   = context.dataStore.data.map { it[KEY_LAST_REFRESH] ?: 0L }
    val webDavUrl: Flow<String>       = context.dataStore.data.map { it[KEY_WEBDAV_URL] ?: "" }
    val webDavUser: Flow<String>      = context.dataStore.data.map { it[KEY_WEBDAV_USER] ?: "" }
    val webDavPass: Flow<String>      = context.dataStore.data.map { it[KEY_WEBDAV_PASS] ?: "" }

    suspend fun saveCookie(cookie: String) =
        context.dataStore.edit { it[KEY_COOKIE] = cookie }

    suspend fun setDarkMode(enabled: Boolean) =
        context.dataStore.edit { it[KEY_DARK_MODE] = enabled }

    suspend fun saveLastRefreshTime(time: Long) =
        context.dataStore.edit { it[KEY_LAST_REFRESH] = time }

    suspend fun saveWebDav(url: String, user: String, pass: String) =
        context.dataStore.edit {
            it[KEY_WEBDAV_URL]  = url.trimEnd('/')
            it[KEY_WEBDAV_USER] = user
            it[KEY_WEBDAV_PASS] = pass
        }
}
