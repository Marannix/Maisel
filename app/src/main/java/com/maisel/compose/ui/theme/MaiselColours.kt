package com.maisel.compose.ui.theme

import androidx.compose.ui.graphics.Color
import com.maisel.ui.theme.Alpha

/**
 * Replace MaiselColour with this class
 */

/**
 * [MaiselColours] Palette is random, UI/UX is an unknown territory for me ¯\_(ツ)_/¯
 * https://mycolor.space/?hex=%233F71AE&sub=1
 */
object MaiselColours {

    val Primary10 = Color(0xFF2B3648)
    val Primary20 = Color(0xFF195E77)
    val Primary30 = Color(0xFF008A93)
    val Primary90 = Color(0xFFD5DBFA)
    val Primary95 = Color(0xFFE6E9FC)

    val Grey10 = Color(0xFF72767E)
    val Grey20 = Color(0xFFDBDDE1)
    val Grey60 = Color(0xFFE9EAED)
    val Grey90 = Color(0xFF272A30)

    val Orange80 = Color(0xFFFBF4DD)

    val Green = Color(0xFF078362)

    val Red10 = Color(0xFFFF3742)
    val Red90 = Color(0xFFF5D0D9)

    val Neutral10 = Color(0xFF14161F)
    val Neutral90 = Color(0xFFD1D2D4)

    val Black = Color(0xFF000000)

    val BVlack = Color(0xFF191D24)

    val White = Color(0xFFFFFFFF)

    val Disabled1 = Color(0xFF3A3C3F)
    val Disabled2 = Color(0xFFDFE2E6)

    val Overlay = Neutral10.copy(alpha = Alpha.Medium)

    internal val Light = object : Variant {

        override val primary = Primary10
        override val onPrimary = White
        override val primaryContainer = White
        override val onPrimaryContainer = Primary10

        override val secondary = Primary10
        override val onSecondary = White

        override val green = Green

        override val disabled = Disabled2

        override val error = Red10
        override val onError = White

        override val background = White
        override val onBackground = Neutral10

        override val surface = White
        override val onSurface = Neutral10

        override val borders = Grey20

        override val inputBackground = Grey60

        override val barsBackground = Primary30
        override val bottomBarsBackground = White

        override val lowEmphasis = Grey10
        override val highEmphasis = Black
    }

    internal val Dark = object : Variant {

        override val primary = Primary90
        override val onPrimary = Primary10
        override val primaryContainer = Primary10
        override val onPrimaryContainer = Primary95

        override val secondary = Primary10
        override val onSecondary = White

        override val green = Green

        override val disabled = Disabled1

        override val error = Red90
        override val onError = Red10

        override val background = BVlack
        override val onBackground = Neutral90

        override val surface = Neutral10
        override val onSurface = Neutral90

        override val borders = Grey90

        override val inputBackground = Grey60

        override val barsBackground = Neutral10
        override val bottomBarsBackground = Primary10

        override val lowEmphasis = Black
        override val highEmphasis = Grey10
    }
}

internal interface Variant {
    val primary: Color
    val onPrimary: Color
    val primaryContainer: Color
    val onPrimaryContainer: Color

    val secondary: Color
    val onSecondary: Color

    val green: Color

    val disabled: Color

    val error: Color
    val onError: Color

    val background: Color
    val onBackground: Color

    val surface: Color
    val onSurface: Color

    val inputBackground: Color

    val borders: Color

    val barsBackground: Color
    val bottomBarsBackground: Color

    val lowEmphasis: Color
    val highEmphasis: Color
}
