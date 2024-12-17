package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),          // Purple
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBB86FC), // Light Purple
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF03DAC6),        // Teal
    onSecondary = Color.Black,
    background = Color(0xFFFFFFFF),       // White
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),
    onSurface = Color.Black,
)

// Dark theme color scheme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),          // Light Purple
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3700B3), // Dark Purple
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF03DAC6),        // Teal
    onSecondary = Color.Black,
    background = Color(0xFF121212),       // Dark Gray
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White,
)


@Composable
fun ArtAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}