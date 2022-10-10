package com.maisel.compose.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Replace MaiselColour with this class
 */

/**
 * [MaiselColours] Palette is random, UI/UX is an unknown territory for me ¯\_(ツ)_/¯
 * https://mycolor.space/?hex=%233F71AE&sub=1
 */
object MaiselColours {

    val Primary10 = Color(0xFF3F71AE)
    val Primary20 = Color(0xFF0095C8)
    val Primary30 = Color(0xFF00B7C7)
    val Primary90 = Color(0xFFD5DBFA)
    val Primary95 = Color(0xFFE6E9FC)

    val Grey10 = Color(0xFF72767E)
    val Grey20 = Color(0xFFDBDDE1)

    val Orange80 = Color(0xFFFBF4DD)

    val Green = Color(0xFF078362)

    val Red10 = Color(0xFFFF3742)
    val Red90 = Color(0xFFF5D0D9)

    val Neutral10 = Color(0xFF171A27)
    val Neutral90 = Color(0xFFD1D2D4)

    val Black = Color(0xFF000000)

    val White = Color(0xFFFFFFFF)

    val Disabled1 = Color(0xFF3A3C3F)
    val Disabled2 = Color(0xFFDFE2E6)


    internal val Light = object : Variant {

        override val primary = Primary30
        override val onPrimary = White
        override val primaryContainer = Primary95
        override val onPrimaryContainer = Primary10

        override val green = Green

        override val disabled = Disabled2

        override val error = Red10
        override val onError = White

        override val background = White
        override val onBackground = Neutral10

        override val surface = White
        override val onSurface = Neutral10
    }

    internal val Dark = object : Variant {

        override val primary = Primary90
        override val onPrimary = Primary10
        override val primaryContainer = Primary20
        override val onPrimaryContainer = Primary95

        override val green = Green

        override val disabled = Disabled1

        override val error = Red90
        override val onError = Red10

        override val background = Neutral10
        override val onBackground = Neutral90

        override val surface = Neutral10
        override val onSurface = Neutral90
    }
}

internal interface Variant {
    val primary: Color
    val onPrimary: Color
    val primaryContainer: Color
    val onPrimaryContainer: Color

    val green: Color

    val disabled: Color

    val error: Color
    val onError: Color

    val background: Color
    val onBackground: Color

    val surface: Color
    val onSurface: Color
}
