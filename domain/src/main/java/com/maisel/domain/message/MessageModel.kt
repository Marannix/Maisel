package com.maisel.domain.message

data class MessageModel(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: String
)
