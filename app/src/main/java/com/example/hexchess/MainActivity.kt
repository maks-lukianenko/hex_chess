package com.example.hexchess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hexchess.ui.theme.HexchessTheme
import com.example.hexchess.frontend.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HexchessTheme {
                AppNavHost()
            }
        }
    }
}





