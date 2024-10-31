package com.example.hexchess.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hexchess.authorization.LoginScreen
import com.example.hexchess.authorization.RegistrationScreen
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
    data object LoginMenu : Screen("LoginScreen")
    data object RegisterMenu : Screen("RegistrationScreen")
    data object MainMenu : Screen("MainMenuScreen")
    data object OnlineGame : Screen("OnlineGameScreen")
}

val screens = listOf(
    Screen.LoginMenu,
    Screen.RegisterMenu,
    Screen.OnlineGame,
    Screen.MainMenu,
)

@Composable
fun HandleScreen(screen: Screen, navController: NavHostController) {
    when (screen) {
        is Screen.LoginMenu -> LoginScreen(navController = navController)
        is Screen.RegisterMenu -> RegistrationScreen(navController = navController)
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

fun NavController.navigateToLoginMenu() {
    navigate("LoginScreen")
}

fun NavController.navigateToRegistrationMenu() {
    navigate("RegistrationScreen")
}