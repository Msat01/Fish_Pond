package com.example.fishPond.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkGray,      // Used mostly for backgrounds
    secondary = White,        // Used for UI elements like buttons
    background = Color.Black,
    surface = Color.DarkGray,
    onPrimary = Color.White, // Text on primary color
    onSecondary = Color.Black, // Text on secondary color
    onBackground = Color.White, // Text on background color
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkGray,     // Used mostly for backgrounds
    secondary = LightGreen,        // Used for UI elements like buttons
    background = White,
    surface = LightGray,
    onPrimary = White, // Text on primary color
    onSecondary = DarkGray, // Text on secondary color
    onBackground = DarkGray, // Text on background color
    onSurface = DarkGray,
    )

@Composable
fun FishPondTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
