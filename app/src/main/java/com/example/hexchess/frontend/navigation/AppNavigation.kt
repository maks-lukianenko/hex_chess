package com.example.hexchess.frontend.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hexchess.backend.gamemanager.GameManager
import com.example.hexchess.frontend.authorization.EnterScreen
import com.example.hexchess.frontend.authorization.LoginScreen
import com.example.hexchess.frontend.authorization.RegistrationScreen
import com.example.hexchess.frontend.mainmenu.HistoryScreen
import com.example.hexchess.frontend.mainmenu.MainMenuScreen
import com.example.hexchess.frontend.onlinegame.OnlineGameScreen

/*
 !!! IMPORTANT !!!
 if you adding new screen you need
 1) add object in Screen
 2) add it to list screens
 3) add handle for that screen
 4) add function to navigate to that screen
 */

sealed class Screen(val route: String) {
    data object EnterMenu : Screen("EnterScreen")
    data object LoginMenu : Screen("LoginScreen")
    data object RegisterMenu : Screen("RegistrationScreen")
    data object MainMenu : Screen("MainMenuScreen")
    data object OnlineGame : Screen("OnlineGameScreen")
    data object MatchHistory : Screen("HistoryScreen")
}

val screens = listOf(
    Screen.EnterMenu,
    Screen.OnlineGame,
    Screen.MatchHistory,
    Screen.LoginMenu,
    Screen.RegisterMenu,
    Screen.MainMenu,
)

@Composable
fun HandleScreen(screen: Screen, navController: NavHostController, gameManager: GameManager) {
    when (screen) {
        is Screen.EnterMenu -> EnterScreen(navController = navController)
        is Screen.LoginMenu -> LoginScreen(navController = navController)
        is Screen.RegisterMenu -> RegistrationScreen(navController = navController)
        is Screen.MainMenu -> MainMenuScreen(navController = navController, gameManager = gameManager)
        is Screen.OnlineGame -> OnlineGameScreen(navController = navController, gameManager = gameManager)
        is Screen.MatchHistory -> HistoryScreen(navController = navController)
    }
}

fun NavController.navigateToMainMenu() {
    navigate("MainMenuScreen") {
        popUpTo(0) { inclusive = true }
    }
}

fun NavController.navigateToOnlineGame() {
    navigate("OnlineGameScreen") {
        popUpTo(0) { inclusive = true }
    }
}

fun NavController.navigateToLoginMenu() {
    navigate("LoginScreen")
}

fun NavController.navigateToRegistrationMenu() {
    navigate("RegistrationScreen")
}

fun NavController.navigateToEnterMenu() {
    navigate("EnterScreen") {
        popUpTo(0) { inclusive = true }
    }
}

fun NavController.navigateToMatchHistory() {
    navigate("HistoryScreen") {
        popUpTo(0) { inclusive = true }
    }
}