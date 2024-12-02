package com.example.hexchess.frontend.mainmenu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hexchess.backend.gamemanager.Game
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class MainMenuViewModel : ViewModel() {
    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games

    private val client = OkHttpClient()

    fun fetchGames(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = "http://34.159.110.3:8000/game-manager/games/" // Replace with your API URL

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token") // Replace if using authentication
                .build()

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val gameListType = object : TypeToken<List<Game>>(){}.type
                        Log.d("MainMenuViewModel", responseBody)
                        val games: List<Game> = Gson().fromJson(responseBody, gameListType)
                        _games.value = games
                        Log.d("MainMenuViewModel", _games.value.toString())
                    }
                } else {
                    // Handle API error
                    Log.d("MainMenuViewModel", "Error: ${response.code}")
                }
            } catch (e: Exception) {
                // Handle network or JSON parsing error
                e.printStackTrace()
            }
        }
    }
}