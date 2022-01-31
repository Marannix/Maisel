package com.maisel.compose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.maisel.ui.QuickSand

/**
 * Contains all the typography we provide for our components.
 *
 * @param title1 Used for big titles, like the image attachment overlay text.
 * @param title3Bold Used for titles of app bars and bottom bars.
 * @param body Used for body content, such as messages.
 * @param bodyItalic Used for body content, italicized, like deleted message components.
 * @param bodyBold Used for emphasized body content, like small titles.
 * @param footnote Used for footnote information, like timestamps.
 * @param footnoteItalic Used for footnote information that's less important, like the deleted message text.
 * @param footnoteBold Used for footnote information in certain important items, like the thread reply text, or user
 * info components.
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
     ) {

    companion object {
        /**
         * Builds the default typography set for our theme, with the ability to customize the font family.
         *
         * @param fontFamily The font that the users want to use for the app.
         * @return [MaiselTypography] that holds all the default text styles that we support.
         */
        
        fun defaultTypography(fontFamily: FontFamily? = null): MaiselTypography = MaiselTypography(
            h1 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W500,
                fontSize = 30.sp,
            ),
            h2 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W500,
                fontSize = 24.sp,
            ),
            h3 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W500,
                fontSize = 20.sp,
            ),
            h4 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W400,
                fontSize = 18.sp,
            ),
            h5 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
            ),
            h6 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
            ),
            subtitle1 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
            ),
            subtitle2 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W300,
                fontSize = 14.sp,
            ),
            body1 = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            body2 = TextStyle(
                fontFamily = QuickSand,
                fontSize = 14.sp
            ),
            button = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W400,
                fontSize = 15.sp,
                color = Color.White
            ),
            caption = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            ),
            overline = TextStyle(
                fontFamily = QuickSand,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp
            )
        )
    }
}
