package com.example.hexchess.frontend.authorization

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.hexchess.frontend.navigation.navigateToMainMenu
import com.example.hexchess.ui.theme.DeepBlueGreen
import com.example.hexchess.ui.theme.LightTurquoise

private val viewModel = AuthorizationViewModel()

@Composable
fun RegistrationScreen(navController: NavHostController = rememberNavController()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    

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
                text = "Sign up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
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
                modifier = Modifier.background(Color.White),
                value = password,
                onValueChange = { password = it },
                textStyle = TextStyle(fontSize = 18.sp),
                decorationBox = { innerTextField ->
                    val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier
                                .background(Color.White, shape = MaterialTheme.shapes.small)
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

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                modifier = Modifier.background(Color.White),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                textStyle = TextStyle(fontSize = 18.sp),
                decorationBox = { innerTextField ->
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier
                                .background(Color.White, shape = MaterialTheme.shapes.small)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .weight(1f)
                        ) {
                            if (confirmPassword.isEmpty()) {
                                Text("Confirm password", color = DeepBlueGreen.copy(alpha = 0.5f))
                            }
                            innerTextField()
                        }
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
                onClick = {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Password did not match", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.onRegisterClicked(username, password, context)
                        navController.navigateToMainMenu()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign up", fontSize = 18.sp)
            }
        }
    }
}