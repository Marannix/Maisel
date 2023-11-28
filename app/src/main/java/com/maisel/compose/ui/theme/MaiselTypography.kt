package com.maisel.compose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/**
 * Contains all the typography we provide for our components.
 */
@Immutable
data class MaiselTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val h5: TextStyle,
    val h6: TextStyle,
    val subtitle1: TextStyle,
    val subtitle2: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val button: TextStyle,
    val caption: TextStyle,
    val overline: TextStyle
)
