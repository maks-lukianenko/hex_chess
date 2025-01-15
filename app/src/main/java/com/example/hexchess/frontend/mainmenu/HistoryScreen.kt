package com.example.hexchess.frontend.mainmenu

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hexchess.backend.authorization.TokenManager
import com.example.hexchess.backend.gamemanager.Game
import com.example.hexchess.frontend.navigation.navigateToMainMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel = viewModel()) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val tokenFlow = remember { tokenManager.getToken() }
    val token by tokenFlow.collectAsState(initial = null)

    var showDetails by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = token) {
        token?.let {
            viewModel.fetchGames(it, context)
        }
    }

    val games = viewModel.games.collectAsState()

    BackHandler {
        navController.navigateToMainMenu()
    }

    Scaffold(
        modifier = Modifier.background(Color(0xFFD6E0F5)),
        topBar = {
            TopAppBar(
                title = { Text(text = "Match history", fontSize = 24.sp, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateToMainMenu() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Exit"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (showDetails && viewModel.currentGame > -1) {
            HistoryItem(
                game = games.value[viewModel.currentGame],
                boardState = viewModel.cells,
                onExitClick = { showDetails = false })
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                items(games.value) { game ->
                    games.value.indexOf(game)
                    GameItem(
                        game = game,
                        resetBoard = {
                            viewModel.resetBoard()
                            viewModel.currentGame = games.value.indexOf(game)
                            showDetails = true
                        })
                }
            }
        }
    }
}

@Composable
fun GameItem(game: Game, resetBoard: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                resetBoard()
                Log.d("Match history", game.moves.toString())
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Game Code: ${game.gameCode}", fontSize = 12.sp)
            Text(text = "Player White: ${game.playerWhite}", fontSize = 12.sp)
            Text(text = "Player Black: ${game.playerBlack}", fontSize = 12.sp)
            Text(text = "Winner: ${game.winner ?: "Not ended"}", fontSize = 12.sp)
        }
    }
}