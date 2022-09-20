package com.maisel.compose.ui.components.shape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.chatdetail.MessageItem
import com.maisel.compose.ui.theme.ChatTheme

@Composable
fun RecipientMessageBox(state: MessageItem.ReceiverMessageItem) {
    Column(
        modifier = Modifier
            .wrapContentSize() //mention max width here
    ) {
        Box(
            modifier = Modifier
                .heightIn(30.dp, 400.dp) //mention max height here
                .widthIn(20.dp, 310.dp) //mention max width here
                .clip(
                    shape = RoundedCornerShape(24.dp).copy(
                        bottomStart = ZeroCornerSize,
                    )
                )
                .background(colorResource(R.color.maisel_compose_receiver_chat_background))
                .padding(16.dp)
        ) {
            Text(
                text = state.message,
                color = colorResource(R.color.mirage),
                style = ChatTheme.typography.subtitle1
            )
        }

        Text(
            modifier = Modifier.align(End),
            text = state.time,
            color = ChatTheme.colors.textLowEmphasis,
            style = ChatTheme.typography.subtitle2,
            fontWeight = FontWeight.Normal
        )
    }
}
