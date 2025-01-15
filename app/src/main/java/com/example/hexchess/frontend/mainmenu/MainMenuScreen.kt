package com.example.hexchess.frontend.mainmenu


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hexchess.backend.authorization.TokenManager
import com.example.hexchess.backend.authorization.UserNameManager
import com.example.hexchess.backend.gamemanager.GameManager
import com.example.hexchess.frontend.navigation.navigateToEnterMenu
import com.example.hexchess.frontend.navigation.navigateToMatchHistory
import com.example.hexchess.frontend.navigation.navigateToOnlineGame

private const val TAG = "Main Menu Screen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavController, gameManager: GameManager) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val tokenFlow = remember { tokenManager.getToken() }
    val token by tokenFlow.collectAsState(initial = null)


    val userNameManager = remember { UserNameManager(context) }
    val usernameFlow = remember { userNameManager.getUsername() }
    val username by usernameFlow.collectAsState(initial = null)

    BackHandler {
        navController.navigateToEnterMenu()
    }

    LaunchedEffect(key1 = token, key2 = username) {
        if (token != null) {
            gameManager.token = token

        }
        if (username != null) gameManager.username = username
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Hello, ${if (username != null) username else "Player"}", fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateToEnterMenu() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Exit"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    gameManager.connectToGame(context)
                    navController.navigateToOnlineGame()
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Play Online")
            }
            Button(
                onClick = {
                    navController.navigateToMatchHistory()
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Game History")
            }
        }
    }
}