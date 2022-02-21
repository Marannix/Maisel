package com.maisel.chat.composables

sealed class MessageItem {
    abstract val uid: String
    abstract val message: String
    abstract val timestamp: String

    class SenderMessageItem(
        override val uid: String,
        override val message: String,
        override val timestamp: String
    ) : MessageItem()

    class ReceiverMessageItem(
        override val uid: String,
        override val message: String,
        override val timestamp: String
    ) : MessageItem()
}
