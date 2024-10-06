package com.example.hexchess.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Use the SetupNavigation composable from AppNavigation.kt
    SetupNavigation(navController = navController)

    // Add any additional UI elements or composables here
}

@Composable
fun SetupNavigation(navController: NavHostController) {
    var initScreen = screens.first().route
    NavHost(
        navController = navController,
        startDestination = initScreen
    ) {
        // Use a loop to set up composable destinations
        screens.forEach { screen ->
            composable(screen.route) {
                // Call a function to handle each screen
                HandleScreen(screen, navController)
            }
        }
    }
}