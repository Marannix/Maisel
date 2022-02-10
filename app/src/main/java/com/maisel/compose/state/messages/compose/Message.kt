package com.maisel.compose.state.messages.compose

import com.maisel.domain.message.MessageModel

data class Message(
    val input: String,
    val senderRoom: String,
    val model: MessageModel
)
