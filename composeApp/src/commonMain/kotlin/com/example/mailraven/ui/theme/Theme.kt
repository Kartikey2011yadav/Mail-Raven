package com.example.mailraven.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryWhite,
    secondary = AccentBlue,
    tertiary = TextLightGray,
    background = MidnightBlue,
    surface = SurfaceDark,
    onPrimary = MidnightBlue,
    onSecondary = TextWhite,
    onTertiary = MidnightBlue,
    onBackground = TextWhite,
    onSurface = TextWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlack,
    secondary = AccentBlue,
    tertiary = TextDarkGray,
    background = CloudBlue,
    surface = CloudWhite,
    onPrimary = CloudWhite,
    onSecondary = CloudWhite,
    onTertiary = CloudWhite,
    onBackground = TextBlack,
    onSurface = TextBlack,
)

@Composable
fun MailRavenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
