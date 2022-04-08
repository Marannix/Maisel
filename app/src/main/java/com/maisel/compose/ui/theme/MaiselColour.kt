package com.maisel.compose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.maisel.R

/**
 * Contains all the colors in our palette. Each color is used for various things an can be changed to
 * customize the app design style.
 * @param textHighEmphasis Used for main text and active icon status.
 * @param textLowEmphasis Used for secondary text and default icon state
 * @param disabled Used for disabled icons and empty states.
 * @param borders Used for borders
 * @param inputBackground Used for the input background smf section headings.
 * @param barsBackground Used for button text, top and bottom bar background and other user messages.
 * @param appBackground Used for the default app background
 * @param overlay Used for general overlays
 * @param primaryAccent Used for selected icon state, call to actions, white buttons text and links.
 * @param errorAccent Used for error text labels, notification badges and disruptive action text and icons.
 */
@Immutable
data class MaiselColour(
    val textHighEmphasis: Color,
    val textLowEmphasis: Color,
    val disabled: Color,
    val borders: Color,
    val inputBackground: Color,
    val appBackground: Color,
    val onAppBackground: Color,
    val overlay: Color,
    val primaryAccent: Color,
    val onPrimaryAccent: Color,
    val secondaryAccent: Color,
    val errorAccent: Color,
    val barsBackground: Color
) {

    companion object {
        /**
         * Provides the default colors for the light mode of the app.
         *
         * @return A [MaiselColour] instance holding our color palette.
         */
        @Composable
        fun defaultColors(): MaiselColour = MaiselColour(
            textHighEmphasis = colorResource(R.color.maisel_compose_text_high_emphasis),
            textLowEmphasis = colorResource(R.color.maisel_compose_text_low_emphasis),
            disabled = colorResource(R.color.maisel_compose_disabled),
            borders = colorResource(R.color.maisel_compose_borders),
            barsBackground = colorResource(R.color.maisel_compose_bars_background),
            inputBackground = colorResource(R.color.maisel_compose_input_background),
            appBackground = colorResource(R.color.maisel_compose_app_background),
            onAppBackground = colorResource(R.color.maisel_compose_on_app_background),
            overlay = colorResource(R.color.maisel_compose_overlay_regular),
            primaryAccent = colorResource(R.color.maisel_compose_primary_accent),
            onPrimaryAccent = colorResource(R.color.maisel_compose_on_primary_accent),
            secondaryAccent = colorResource(R.color.maisel_compose_secondary_accent),
            errorAccent = colorResource(R.color.maisel_compose_error_accent)
        )

        /**
         * Provides the default colors for the dark mode of the app.
         *
         * @return A [MaiselColour] instance holding our color palette.
         */
        @Composable
        fun defaultDarkColors(): MaiselColour = MaiselColour(
            textHighEmphasis = colorResource(R.color.maisel_compose_text_high_emphasis_dark),
            textLowEmphasis = colorResource(R.color.maisel_compose_text_low_emphasis_dark),
            disabled = colorResource(R.color.maisel_compose_disabled_dark),
            borders = colorResource(R.color.maisel_compose_borders_dark),
            barsBackground = colorResource(R.color.maisel_compose_bars_background_dark),
            inputBackground = colorResource(R.color.maisel_compose_input_background_dark),
            appBackground = colorResource(R.color.maisel_compose_app_background_dark),
            onAppBackground = colorResource(R.color.maisel_compose_on_app_background_dark),
            overlay = colorResource(R.color.maisel_compose_overlay_regular_dark),
            primaryAccent = colorResource(R.color.maisel_compose_primary_accent_dark),
            onPrimaryAccent = colorResource(R.color.maisel_compose_on_primary_accent_dark),
            secondaryAccent = colorResource(R.color.maisel_compose_secondary_accent_dark),
            errorAccent = colorResource(R.color.maisel_compose_error_accent_dark),
        )
    }
}
