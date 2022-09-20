package com.maisel.compose.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.maisel.compose.ui.theme.ChatTheme.colors

//TODO: Update this file
/**
 * Local providers for various properties we connect to our components, for styling.
 */
private val LocalColors = compositionLocalOf<MaiselColour> {
    error("No colors provided! Make sure to wrap all usages of Maisel components in a ChatTheme.")
}

private val LocalTypography = compositionLocalOf<MaiselTypography> {
    error("No typography provided! Make sure to wrap all usages of Maisel components in a ChatTheme.")
}

/**
 * Our theme that provides all the important properties for styling to the user.
 *
 * @param isInDarkMode If we're currently in the dark mode or not. Affects only the default color palette that's
 * provided. If you customize [colors], make sure to add your own logic for dark/light colors.
 * @param colours The set of colors we provide, wrapped in [MaiselColour].
 * @param dimens The set of dimens we provide, wrapped in [MaiselDimens].
 * @param typography The set of typography styles we provide, wrapped in [MaiselTypography].
 * @param shapes The set of shapes we provide, wrapped in [MaiselShapes].
 * @param content The content shown within the theme wrapper.
 */
@Composable
fun ChatTheme(
   // isInDarkMode: Boolean = isSystemInDarkTheme(),
    isInDarkMode: Boolean = false,
    colours: MaiselColour = if (isInDarkMode) MaiselColour.defaultDarkColors() else MaiselColour.defaultColors(),
    typography: MaiselTypography = MaiselTypography.defaultTypography(),
   // shapes: MaiselShapes = MaiselShapes.defaultShapes(),
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = colours.primaryAccent,
            darkIcons = isInDarkMode
        )
    }

    CompositionLocalProvider(
        LocalColors provides colours,
        LocalTypography provides typography,
    ) {
        content()
    }
}

 object ChatTheme {

    /**
     * These represent the default ease-of-use accessors for different properties used to style and customize the app
     * look and feel.
     */
    val colors: MaiselColour
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

     val typography: MaiselTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

}
