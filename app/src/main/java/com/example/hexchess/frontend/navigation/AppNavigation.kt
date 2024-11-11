package com.example.hexchess.frontend.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hexchess.frontend.authorization.EnterScreen
import com.example.hexchess.frontend.authorization.LoginScreen
import com.example.hexchess.frontend.authorization.RegistrationScreen
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
}

val screens = listOf(
    Screen.OnlineGame,
    Screen.EnterMenu,
    Screen.LoginMenu,
    Screen.RegisterMenu,
    Screen.MainMenu,
)

@Composable
fun HandleScreen(screen: Screen, navController: NavHostController) {
    when (screen) {
        is Screen.EnterMenu -> EnterScreen(navController = navController)
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

fun NavController.navigateToEnterMenu() {
    navigate("EnterScreen")
}