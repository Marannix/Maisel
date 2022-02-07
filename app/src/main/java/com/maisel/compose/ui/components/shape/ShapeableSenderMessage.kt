package com.maisel.compose.ui.components.shape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShapeableSenderMessage() {
    Box(
        modifier = Modifier
            .heightIn(30.dp, 400.dp) //mention max height here
            .widthIn(20.dp, 310.dp) //mention max width here
            .clip(shape =  RoundedCornerShape(16.dp).copy(
                bottomEnd = ZeroCornerSize,
            ))
            .background(Color.Red)
    )
}
