package com.maisel.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.maisel.ui.typography

@Immutable
data class ExtendedColors(
    val barsBackground: Color,
    val bottomBarsBackground: Color,
    val borders: Color,
    val disabled: Color,
    val lowEmphasis: Color,
    val highEmphasis: Color,
    val inputBackground: Color,
)

private val LightColorPalette = lightColors(
    primary = MaiselColours.Light.primary,
    onPrimary = MaiselColours.Light.onPrimary,

    error = MaiselColours.Light.error,
    onError = MaiselColours.Light.onError,

    background = MaiselColours.Light.background,
    onBackground = MaiselColours.Light.onBackground,

    surface = MaiselColours.Light.surface,
    onSurface = MaiselColours.Light.onSurface,
)

/**
 * Extending the color definitions for light color
 */
internal val ExtendedLightColorPalette = ExtendedColors(
    barsBackground = MaiselColours.Light.primaryContainer,
    bottomBarsBackground = MaiselColours.Light.primaryContainer,
    borders = MaiselColours.Light.primaryContainer,
    disabled = MaiselColours.Light.disabled,
    lowEmphasis = MaiselColours.Light.lowEmphasis,
    highEmphasis = MaiselColours.Light.highEmphasis,
    inputBackground = MaiselColours.Light.inputBackground,
)

/**
 * Color definitions for dark mode.
 */
private val DarkColorPalette = darkColors(
    primary = MaiselColours.Dark.primary,
    onPrimary = MaiselColours.Dark.onPrimary,

    error = MaiselColours.Dark.error,
    onError = MaiselColours.Dark.onError,

    background = MaiselColours.Dark.background,
    onBackground = MaiselColours.Dark.onBackground,

    surface = MaiselColours.Dark.surface,
    onSurface = MaiselColours.Dark.onSurface,
)

/**
 * Extending the color definitions for dark color
 */
val ExtendedDarkColorPalette = ExtendedColors(
    barsBackground = MaiselColours.Dark.primaryContainer,
    bottomBarsBackground = MaiselColours.Dark.primaryContainer,
    borders = MaiselColours.Dark.primaryContainer,
    disabled = MaiselColours.Dark.disabled,
    lowEmphasis = MaiselColours.Dark.lowEmphasis,
    highEmphasis = MaiselColours.Dark.highEmphasis,
    inputBackground = MaiselColours.Dark.inputBackground,
)

internal val LocalExtendedColors = staticCompositionLocalOf<ExtendedColors> {
    throw IllegalArgumentException("ExtendedColors has not been initialised in the theme.")
}

@Suppress("unused")
val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current

/**
 * Theme that provides all the important properties for styling to the user.
 *
 * @param darkTheme If we're currently in the dark mode or not. Affects only the default color palette that's
 * provided.
 * @param content The content shown within the theme wrapper.
 */
@Composable
fun MaiselTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val extendedColors = if (darkTheme) ExtendedDarkColorPalette else ExtendedLightColorPalette

    LocalContentAlpha

    MaterialTheme(
        colors = colors,
        typography = typography,
    ) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = MaterialTheme.colors.background,
            darkIcons = !darkTheme
        )

        CompositionLocalProvider(
            LocalExtendedColors provides extendedColors,
            content = content
        )
    }
}
