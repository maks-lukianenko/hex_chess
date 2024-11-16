package com.example.hexchess.frontend.authorization

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hexchess.backend.authorization.LoginRequest
import com.example.hexchess.backend.authorization.RegisterRequest
import com.example.hexchess.backend.authorization.TokenManager
import com.example.hexchess.backend.authorization.TokenResponse
import com.example.hexchess.backend.authorization.UserNameManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

private const val TAG = "Authorization View Model"

class AuthorizationViewModel : ViewModel() {
    private val JSON = "application/json; charset=utf-8".toMediaType()
    private val gson = Gson()
    private val client = OkHttpClient()


    fun onLoginClicked(username: String, password: String, context: Context, onLoginSuccess: () -> Unit, onLoginFailure: (String) -> Unit) {
        val loginRequest = LoginRequest(username, password)
        val requestBody = gson.toJson(loginRequest).toRequestBody(JSON)
        val tokenManager = TokenManager(context)
        val userNameManager = UserNameManager(context)

        val request = Request.Builder()
            .url("http://192.168.56.1:8000/players/login/")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                    if (it.isSuccessful) {
                        val tokenResponse = gson.fromJson(it.body?.string(), TokenResponse::class.java)
                        CoroutineScope(Dispatchers.IO).launch {
                            tokenManager.saveToken(tokenResponse.accessToken)
                            userNameManager.saveUsername(username)
                        }
                        Log.d(TAG, "Login successful: ${tokenResponse.accessToken}")
                        Handler(Looper.getMainLooper()).post {
                            onLoginSuccess()
                        }
                    } else {
                        val errorMessage = it.body?.string() ?: "Unknown error occurred"
                        Log.d(TAG, "Login failed: $errorMessage")
                        Handler(Looper.getMainLooper()).post {
                            onLoginFailure(errorMessage)
                        }

                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(TAG, "Login error: ${e.message}")
                Handler(Looper.getMainLooper()).post {
                    onLoginFailure(e.message ?: "Network error")
                }
            }
        })
    }

    fun onRegisterClicked(username: String, email: String, password: String, context: Context) {
        val registerRequest = RegisterRequest(username, email, password)
        val requestBody = gson.toJson(registerRequest).toRequestBody(JSON)
        val tokenManager = TokenManager(context)
        val userNameManager = UserNameManager(context)

        val request = Request.Builder()
            .url("http://169.254.101.110:8000/players/register/")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                    if (it.isSuccessful) {
                        val tokenResponse = gson.fromJson(it.body?.string(), TokenResponse::class.java)
                        CoroutineScope(Dispatchers.IO).launch {
                            tokenManager.saveToken(tokenResponse.accessToken)
                            userNameManager.saveUsername(username)
                        }
                        Log.d(TAG, "Register successful: ${tokenResponse.accessToken}")
                        // Save tokens and proceed to next screen
                    } else {
                        Log.d(TAG, "Register failed: ${it.message}")
                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(TAG, "Register error: ${e.message}")
            }
        })
    }
}