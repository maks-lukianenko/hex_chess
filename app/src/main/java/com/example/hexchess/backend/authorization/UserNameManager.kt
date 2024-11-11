package com.example.hexchess.backend.authorization

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hexchess.frontend.navigation.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val  USERNAME_KEY = stringPreferencesKey("username")

class UserNameManager(private val context: Context) {

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    fun getUsername(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USERNAME_KEY]
        }
    }
}