package com.maisel.domain.message

import java.util.*

data class ChatDataModel(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val time: String,
    val date: String = Date().date.toString()
)
