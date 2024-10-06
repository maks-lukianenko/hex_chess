package com.example.hexchess.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hexchess.mainmenu.MainMenuScreen
import com.example.hexchess.onlinegame.OnlineGameScreen

/*
 !!! IMPORTANT !!!
 if you adding new screen you need
 1) add object in Screen
 2) add it to list screens
 3) add handle for that screen
 4) add function to navigate to that screen
 */

sealed class Screen(val route: String) {
    object MainMenu : Screen("MainMenuScreen")
    object OnlineGame : Screen("OnlineGameScreen")
}

val screens = listOf(
    Screen.OnlineGame,
    Screen.MainMenu,

)

@Composable
fun HandleScreen(screen: Screen, navController: NavHostController) {
    when (screen) {
        is Screen.MainMenu -> MainMenuScreen(navController = navController)
        is Screen.OnlineGame -> OnlineGameScreen(navController = navController)
    }
}

fun NavController.navigateToMainMenu() {
    navigate("MainMenuScreen")
}
fun NavController.navigateToOnlineGame() {
    navigate("OnlineGameScreen")
}