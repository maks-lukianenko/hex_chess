package com.example.hexchess.frontend.mainmenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HistoryScreen(navController: NavController) {
    val data = mutableListOf<String>()

    for (i in 1..100) {
        data.add("Item number $i")
    }

    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            items(data) { item ->
                Text(
                    text = item,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
            }
        }
    }
}