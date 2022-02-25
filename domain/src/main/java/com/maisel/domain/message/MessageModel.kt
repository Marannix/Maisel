package com.maisel.domain.message

import java.util.*

data class MessageModel(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val time: String,
    val date: String = Date().date.toString()
)
