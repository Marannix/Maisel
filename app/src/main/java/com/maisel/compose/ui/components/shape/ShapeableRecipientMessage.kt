package com.maisel.compose.ui.components.shape

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.compose.ui.theme.ChatTheme

@Composable
fun RecipientMessageBox(content: @Composable BoxScope.() -> Unit) {
    Column(
        modifier = Modifier
            //.wrapContentSize() //mention max width here
            .heightIn(30.dp, 400.dp) //mention max height here
            .widthIn(20.dp, 310.dp) //mention max width here
    ) {
        Box(
            modifier = Modifier
                .heightIn(30.dp, 400.dp) //mention max height here
                .widthIn(20.dp, 310.dp) //mention max width here
                .clip(
                    shape = RoundedCornerShape(16.dp).copy(
                        bottomStart = ZeroCornerSize,
                    )
                )
                .background(Color.White)
                .border(
                    BorderStroke(1.dp, colorResource(R.color.maisel_compose_borders)),
                    RoundedCornerShape(16.dp).copy(
                        bottomStart = ZeroCornerSize,
                    )
                )
                .padding(8.dp)
        ) {
            content()
        }

        Text(
            text = "13:37 pm",
            textAlign = TextAlign.End,
            color = ChatTheme.colors.textLowEmphasis,
            style = MaterialTheme.typography.subtitle2
        )
    }

}
