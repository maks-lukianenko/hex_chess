package com.example.hexchess.ui.theme

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightBlueTeal,      // Primary color for dark mode
    onPrimary = DeepGreenBlue,
    primaryContainer = DarkBlueGreen,
    onPrimaryContainer = Color.White,
    secondary = GrayishTeal,
    onSecondary = Color.White,
    surface = DeepGreenBlue,
    onSurface = LightBlueTeal,
    background = DarkBlueGreen,
    onBackground = LightBlueTeal,
)

val LightTurquoise = Color(0xFFE0F4F5)
val DeepBlueGreen = Color(0xFF015366)

private val LightColorScheme = lightColorScheme(
    primary = DarkBlueGreen,     // Primary color for light mode
    onPrimary = Color.White,
    primaryContainer = LightBlueTeal,
    onPrimaryContainer = DeepGreenBlue,
    secondary = GrayishTeal,
    onSecondary = Color.White,
    surface = LightTeal,
    onSurface = DeepGreenBlue,
    background = LightBlueTeal,
    onBackground = DarkBlueGreen,
)

@Composable
fun HexchessTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}