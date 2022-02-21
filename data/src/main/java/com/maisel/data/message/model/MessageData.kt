package com.maisel.data.message.model

data class MessageData(
    val uid: String,
    val message: String,
    val timestamp: Long
) {
    constructor() : this ("", "", 0L) {

    }
}
