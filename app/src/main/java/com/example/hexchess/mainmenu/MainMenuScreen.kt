package com.example.hexchess.mainmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hexchess.navigation.navigateToOnlineGame

@Composable
fun MainMenuScreen(navController: NavHostController = rememberNavController()) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { navController.navigateToOnlineGame() }) {
                Text(text = "Click")
            }
        }
    }
}