package com.maisel.data.message.model

data class MessageData(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: Long
) {
    constructor() : this ("", "", "", 0L) {

    }
}
