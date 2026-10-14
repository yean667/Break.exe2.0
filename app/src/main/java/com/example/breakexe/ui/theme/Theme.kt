package com.example.breakexe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PinkBrand,
    onPrimary = Color.White,
    primaryContainer = PinkBrandLight,
    onPrimaryContainer = PinkBrandDark,
    secondary = BluePrimary,
    onSecondary = Color.White,
    secondaryContainer = BlueDamageCardBg,
    onSecondaryContainer = BluePrimary,
    background = CreamBackground,
    onBackground = TextMocha,
    surface = CreamCardBg,
    onSurface = TextMocha,
    surfaceVariant = PinkBrandLight,
    onSurfaceVariant = TextMochaMuted,
    outline = CreamCardBorder
)

@Composable
fun BreakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // break.exe uses a cute warm pastel aesthetic consistently
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
