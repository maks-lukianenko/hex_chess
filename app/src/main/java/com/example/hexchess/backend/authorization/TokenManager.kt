package com.example.hexchess.backend.authorization

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hexchess.frontend.navigation.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val TOKEN_KEY = stringPreferencesKey("access_token")

class TokenManager(private val context: Context) {
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }
}