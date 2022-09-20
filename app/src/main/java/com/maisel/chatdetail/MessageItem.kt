package com.maisel.chatdetail

sealed class MessageItem {
    abstract val uid: String
    abstract val message: String
    abstract val time: String
    abstract val date: String

    class SenderMessageItem(
        override val uid: String,
        override val message: String,
        override val time: String,
        override val date: String
    ) : MessageItem()

    class ReceiverMessageItem(
        override val uid: String,
        override val message: String,
        override val time: String,
        override val date: String
    ) : MessageItem()
}
