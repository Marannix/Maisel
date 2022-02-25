package com.maisel.data.message

import com.maisel.data.message.model.MessageData
import com.maisel.data.utils.DateFormatter
import com.maisel.domain.message.MessageModel

fun MessageData.toMessageModel() : MessageModel {
    return MessageModel(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = DateFormatter().getChatTime(this.timestamp),
        date = DateFormatter().getDate(this.timestamp),
    )
}

fun MessageModel.toMessageData() : MessageData {
    return MessageData(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        timestamp = this.time.toLong()
    )
}
