package com.maisel.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = colorPrimary,
    secondary = colorSecondary,
    surface = colorSurface,
    primaryVariant = colorPrimaryVariant,
    onPrimary = colorOnPrimary,
    //   onSurface = colorOnPrimary,
    // background= colorOnPrimary
)

private val DarkColorPalette = darkColors(
    //main background color
    primary = colorPrimaryDark,
    //text color
    secondary = colorSecondaryDark,
    //background
    surface = colorSurfaceDark,
    primaryVariant = colorPrimaryVariantDark,
    onPrimary = colorOnPrimaryDark,
//    onSurface = colorOnPrimaryDark,
)

@Composable
fun MainTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = HelventicaTypography,
        //    shapes = shapes,
        content = content
    )
}
