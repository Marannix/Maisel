package com.maisel.compose.ui.components.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.*

@Composable
fun LottieAsset(
    asset: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    clipSpec: LottieClipSpec? = null,
    speed: Float = 1f,
    iterations: Int = 1,
    outlineMasksAndMattes: Boolean = false,
    applyOpacityToLayers: Boolean = false,
    enableMergePaths: Boolean = false,
    renderMode: RenderMode = RenderMode.AUTOMATIC,
    maintainOriginalImageBounds: Boolean = false,
    dynamicProperties: LottieDynamicProperties? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    clipToCompositionBounds: Boolean = true,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(asset))
    LottieAnimation(
        composition = composition,
        iterations = iterations,
        modifier = modifier,
        isPlaying = isPlaying,
        restartOnPlay = restartOnPlay,
        clipSpec = clipSpec,
        speed = speed,
        outlineMasksAndMattes = outlineMasksAndMattes,
        applyOpacityToLayers = applyOpacityToLayers,
        enableMergePaths = enableMergePaths,
        renderMode = renderMode,
        maintainOriginalImageBounds = maintainOriginalImageBounds,
        dynamicProperties = dynamicProperties,
        alignment = alignment,
        contentScale = contentScale,
        clipToCompositionBounds = clipToCompositionBounds
    )
}
