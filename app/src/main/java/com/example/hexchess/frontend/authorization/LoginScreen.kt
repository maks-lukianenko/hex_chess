package com.example.hexchess.frontend.authorization

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hexchess.frontend.navigation.navigateToEnterMenu
import com.example.hexchess.frontend.navigation.navigateToMainMenu
import com.example.hexchess.frontend.navigation.navigateToRegistrationMenu
import com.example.hexchess.ui.theme.DeepBlueGreen

private val viewModel = AuthorizationViewModel()

private const val TAG = "Login Screen"

@Composable
fun LoginScreen(navController: NavHostController = rememberNavController()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler {
        navController.navigateToEnterMenu()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
                textStyle = TextStyle(fontSize = 18.sp),
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
                modifier = Modifier.background(Color.White, shape = MaterialTheme.shapes.small),
                value = password,
                onValueChange = { password = it },
                textStyle = TextStyle(fontSize = 18.sp),
                decorationBox = { innerTextField ->
                    val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .weight(1f)
                        ) {
                            if (password.isEmpty()) {
                                Text("Password", color = DeepBlueGreen.copy(alpha = 0.5f))
                            }
                            innerTextField()
                        }
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(
                                imageVector = image,
                                contentDescription = null,
                                tint = DeepBlueGreen)
                        }
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onLoginClicked(
                        username,
                        password,
                        context,
                        onLoginSuccess = {
                            // Navigate to main menu on success
                            navController.navigateToMainMenu()
                            Log.d(TAG, "Login successful, navigating to main menu")
                        },
                        onLoginFailure = { errorMessage ->
                            // Show an error message or handle failure UI
                            Log.d(TAG, "Login failed: $errorMessage")
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Log.d(TAG,"Pressed Login")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier.clickable { navController.navigateToRegistrationMenu() },
                text = "Sign up",
                fontSize = 16.sp,
                color = DeepBlueGreen
            )
        }
    }
}
