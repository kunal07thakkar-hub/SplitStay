
package com.splitstay.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5B8FC9),
    onPrimary = Color.White,
    secondary = SoftLavender,
    onSecondary = SlateText,
    tertiary = MintGreen,
    onTertiary = SlateText,
    background = AppBackground,
    onBackground = SlateText,
    surface = CardWhite,
    onSurface = SlateText,
    surfaceVariant = IceBlue,
    onSurfaceVariant = SecondaryText
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9CC7F0),
    secondary = Color(0xFFC4B5FD),
    tertiary = Color(0xFF86EFAC),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = Color(0xFF0F172A),
    onSecondary = Color(0xFF0F172A),
    onTertiary = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9)
)

@Composable
fun SplitStayTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
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