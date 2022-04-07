package com.maisel.data.message.mapper

import com.maisel.data.message.entity.MessageEntity
import com.maisel.data.message.entity.RecentMessageEntity
import com.maisel.data.message.model.MessageData
import com.maisel.data.utils.DateFormatter
import com.maisel.domain.message.ChatDataModel
import com.maisel.domain.message.ChatModel

fun MessageData.toMessageModel(userId: String?): ChatModel {
    return ChatModel(
        userId = userId,
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = DateFormatter().getChatTime(this.timestamp),
        date = DateFormatter().getDate(this.timestamp),
    )
}

fun MessageData.toChatModel(userId: String?): ChatModel {
    return ChatModel(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = DateFormatter().getChatTime(this.timestamp),
        date = DateFormatter().getDate(this.timestamp),
    )
}

fun ChatDataModel.toMessageData() : MessageData {
    return MessageData(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        timestamp = this.time.toLong()
    )
}

fun ChatModel.toRecentMessageEntity() : RecentMessageEntity {
    return RecentMessageEntity(
        userId = requireNotNull(this.userId),
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}

fun RecentMessageEntity.toChatModel() : ChatModel {
    return ChatModel(
        userId = this.userId,
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}

fun ChatModel.toMessageEntity() : MessageEntity {
    return MessageEntity(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}

fun MessageEntity.toChatModel() : ChatModel {
    return ChatModel(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}
