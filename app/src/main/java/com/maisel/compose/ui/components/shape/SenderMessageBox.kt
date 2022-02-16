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
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.chat.composables.MessageItem
import com.maisel.compose.ui.theme.ChatTheme

@Composable
fun SenderMessageBox(state: MessageItem.SenderMessageItem) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .align(End)
                .heightIn(30.dp, 400.dp) //mention max height here
                .widthIn(20.dp, 310.dp) //mention max width here
                .clip(
                    shape = RoundedCornerShape(8.dp).copy(
                        bottomEnd = ZeroCornerSize,
                    )
                )
                .background(Color.White)
                .border(
                    BorderStroke(1.dp, colorResource(R.color.maisel_compose_borders)),
                    RoundedCornerShape(8.dp).copy(
                        bottomEnd = ZeroCornerSize,
                    )
                )
                .padding(8.dp)
        ) {
            Text(
                text = state.message,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold
            )        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "13:37 pm",
            textAlign = TextAlign.End,
            color = ChatTheme.colors.textLowEmphasis,
            style = MaterialTheme.typography.subtitle2
        )
    }
}