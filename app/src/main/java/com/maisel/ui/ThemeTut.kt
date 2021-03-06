package com.maisel.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Primary200,
    primaryVariant = PrimaryVariant,
    background = BlackDark,
    surface = BlackLight,
    onBackground = WhiteLight,
    onSurface = WhiteLight,
    onPrimary = BlackDark
)

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    background = WhiteDark,
    surface = WhiteLight,
    onBackground = BlackDark,
    onSurface = BlackDark,
    onPrimary = WhiteLight
)

@Composable
fun OnboardingTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}