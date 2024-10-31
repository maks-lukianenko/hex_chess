package com.example.hexchess.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hexchess.ui.theme.DeepBlueGreen
import com.example.hexchess.ui.theme.LightTurquoise

private val viewModel = AuthorizationViewModel()

@Composable
fun LoginScreen(navController: NavHostController = rememberNavController()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightTurquoise),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlueGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = username,
                onValueChange = { username = it },
                textStyle = TextStyle(fontSize = 18.sp, color = DeepBlueGreen),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth()
                    ) {
                        if (username.isEmpty()) {
                            Text("Username", color = DeepBlueGreen.copy(alpha = 0.5f))
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                modifier = Modifier.background(Color.White),
                value = password,
                onValueChange = { password = it },
                textStyle = TextStyle(fontSize = 18.sp, color = DeepBlueGreen),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth()
                    ) {
                        if (username.isEmpty()) {
                            Text("Password", color = DeepBlueGreen.copy(alpha = 0.5f))
                        }
                        innerTextField()
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onLoginClicked(username, password) },
                colors = ButtonColors(
                    containerColor = DeepBlueGreen,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                    disabledContainerColor = DeepBlueGreen
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
