package com.maisel.data.message.mapper

import com.maisel.data.message.entity.MessageEntity
import com.maisel.data.message.entity.RecentMessageEntity
import com.maisel.data.message.model.MessageData
import com.maisel.data.utils.DateFormatter
import com.maisel.domain.message.ChatModel
import com.maisel.domain.message.MessageModel

fun MessageData.toMessageModel(userId: String?): MessageModel {
    return MessageModel(
        userId = userId,
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = DateFormatter().getChatTime(this.timestamp),
        date = DateFormatter().getDate(this.timestamp),
    )
}

fun MessageData.toChatModel(): ChatModel {
    return ChatModel(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = DateFormatter().getChatTime(this.timestamp),
        date = DateFormatter().getDate(this.timestamp),
    )
}

fun ChatModel.toMessageData() : MessageData {
    return MessageData(
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        timestamp = this.time.toLong()
    )
}

fun MessageModel.toRecentMessageEntity() : RecentMessageEntity {
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
