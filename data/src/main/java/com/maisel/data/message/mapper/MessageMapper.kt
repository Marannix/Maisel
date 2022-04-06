package com.maisel.data.message.mapper

import com.maisel.data.message.entity.RecentMessageEntity
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

fun MessageModel.toMessageEntity() : RecentMessageEntity {
    return RecentMessageEntity(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}

fun RecentMessageEntity.toMessageModel() : MessageModel {
    return MessageModel(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}
