package com.maisel.data.message

import com.maisel.data.message.model.MessageData
import com.maisel.data.utils.DateFormatter
import com.maisel.domain.message.MessageModel

fun MessageData.toMessageModel() : MessageModel {
    return MessageModel(
        uid = this.uid,
        message = this.message,
        timestamp = DateFormatter().getChatTime(this.timestamp)
    )
}

fun MessageModel.toMessageData() : MessageData {
    return MessageData(
        uid = this.uid,
        message = this.message,
        timestamp = this.timestamp.toLong()
    )
}
