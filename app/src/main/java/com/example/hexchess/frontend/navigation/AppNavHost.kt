package com.example.hexchess.frontend.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hexchess.backend.gamemanager.GameManager

val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val gameManager = remember { GameManager() }

    SetupNavigation(navController = navController, gameManager)
}

@Composable
fun SetupNavigation(navController: NavHostController, gameManager: GameManager) {
    val initScreen = screens.first().route

    NavHost(
        navController = navController,
        startDestination = initScreen
    ) {
        screens.forEach { screen ->
            composable(screen.route) {
                HandleScreen(screen, navController, gameManager)
            }
        }
    }
}