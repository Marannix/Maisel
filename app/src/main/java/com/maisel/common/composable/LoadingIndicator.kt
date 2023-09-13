package com.maisel.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.maisel.compose.ui.theme.MaiselColours

object LoadingIndicator {

    private data class StylingAttributes(
        val backgroundColor: Color,
        val color: Color,
        val systemBarColor: Color,
        val size: Dp,
        val strokeWidth: Dp,
    )

    @Composable
    fun FullScreenLoadingIndicator(
        modifier: Modifier = Modifier,
        currentStatusBarColor: Color,
    ) {
        val statusBarStylingAttributes = getStylingAttributes(currentStatusBarColor)
        val systemUiController = rememberSystemUiController()

        DisposableEffect(Unit) {
            systemUiController.apply {
                setSystemBarsColor(
                    color = statusBarStylingAttributes.systemBarColor,
                    darkIcons = statusBarStylingAttributes.systemBarColor.luminance() > 0.5f
                )
            }

            onDispose {
                systemUiController.apply {
                    setSystemBarsColor(
                        color = statusBarStylingAttributes.systemBarColor,
                        darkIcons = statusBarStylingAttributes.systemBarColor.luminance() > 0.5f
                    )
                }
            }
        }

        Surface(
            modifier = modifier
                .fillMaxSize(),
            color = statusBarStylingAttributes.backgroundColor,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(statusBarStylingAttributes.size),
                    color = statusBarStylingAttributes.color,
                    strokeWidth = statusBarStylingAttributes.strokeWidth,
                )
            }
        }
    }

    @Composable
    private fun getStylingAttributes(
        systemBarColour: Color,
    ): StylingAttributes {
        return StylingAttributes(
            backgroundColor = MaiselColours.Overlay,
            systemBarColor = MaiselColours.Overlay.compositeOver(
                systemBarColour,
            ),
            color = MaiselColours.Dark.primary,
            size = 56.dp,
            strokeWidth = 5.dp,
        )
    }
}
