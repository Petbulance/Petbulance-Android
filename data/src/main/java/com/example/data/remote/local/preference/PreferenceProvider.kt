package com.example.data.remote.local.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceProvider(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("user_preferences")

    suspend fun updateTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[appTheme] = theme
        }
    }

    fun observeTheme(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[appTheme] ?: "DEVICE"
        }
    }

    companion object {
        val appTheme = stringPreferencesKey("app_theme")
    }
}