package com.maisel.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.maisel.domain.database.AppTheme
import com.maisel.ui.settingDarkCardBackgroundColor
import com.maisel.ui.settingDarkCardOnBackgroundColor
import com.maisel.ui.settingLightCardBackgroundColor
import com.maisel.ui.settingLightCardOnBackgroundColor
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
    val cardBackgroundColor: Color,
    val cardOnBackgroundColor: Color,
)

private val LightColorPalette = lightColors(
    primary = MaiselColours.Light.primary,
    onPrimary = MaiselColours.Light.onPrimary,

    secondary = MaiselColours.Light.primary,
    onSecondary = MaiselColours.Light.onPrimary,

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
    cardBackgroundColor = settingLightCardBackgroundColor,
    cardOnBackgroundColor = settingLightCardOnBackgroundColor
)

/**
 * Color definitions for dark mode.
 */
private val DarkColorPalette = darkColors(
    primary = MaiselColours.Dark.primary,
    onPrimary = MaiselColours.Dark.onPrimary,

    secondary = MaiselColours.Dark.primary,
    onSecondary = MaiselColours.Dark.onPrimary,

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
    cardBackgroundColor = settingDarkCardBackgroundColor,
    cardOnBackgroundColor = settingDarkCardOnBackgroundColor
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
    appTheme: AppTheme = AppTheme.SYSTEM_DEFAULT,
    content: @Composable () -> Unit
) {

    val isDarkMode: Boolean = when (appTheme) {
        AppTheme.LIGHT_MODE -> false
        AppTheme.DARK_MODE -> true
        AppTheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
        else -> isSystemInDarkTheme()
    }

    val colors = if (isDarkMode) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val extendedColors =
        if (isDarkMode) ExtendedDarkColorPalette else ExtendedLightColorPalette

    LocalContentAlpha

    MaterialTheme(
        colors = colors,
        typography = typography,
    ) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = MaterialTheme.colors.background,
            darkIcons = !isDarkMode
        )

        CompositionLocalProvider(
            LocalExtendedColors provides extendedColors,
            content = content
        )
    }
}
