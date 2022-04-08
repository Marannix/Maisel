package com.maisel.compose.state.messages.compose

import com.maisel.domain.message.ChatModel

data class Message(
    val input: String,
    val senderRoom: String,
    val model: ChatModel
)
