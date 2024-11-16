package com.example.hexchess.frontend.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hexchess.ui.theme.DarkBlueGreen
import com.example.hexchess.ui.theme.GrayishTeal
import com.example.hexchess.ui.theme.LightBlueTeal
import com.example.hexchess.ui.theme.LightTeal

@Composable
fun LoadingScreen(onCancel: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueTeal),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = LightTeal,
                strokeWidth = 6.dp,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Waiting for opponent...",
                color = GrayishTeal,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlueGreen, // Button background color
                    contentColor = Color.White // Button text color
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.6f) // Set button width to 60% of the screen width
            ) {
                Text("Cancel", fontSize = 16.sp)
            }
        }
    }
}