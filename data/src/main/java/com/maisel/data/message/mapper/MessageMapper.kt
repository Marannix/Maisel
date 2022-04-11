package com.maisel.data.message.mapper

import com.maisel.data.message.entity.MessageEntity
import com.maisel.data.message.entity.RecentMessageEntity
import com.maisel.data.message.model.MessageData
import com.maisel.data.utils.DateFormatter
import com.maisel.domain.message.ChatDataModel
import com.maisel.domain.message.ChatModel

fun MessageData.toMessageModel(userId: String?): ChatModel {
    return ChatModel(
        messageId = messageId,
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
        messageId = this.messageId,
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = DateFormatter().getChatTime(this.timestamp),
        date = DateFormatter().getDate(this.timestamp),
    )
}

fun ChatDataModel.toMessageData(messageId: String) : MessageData {
    return MessageData(
        messageId = messageId,
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        timestamp = this.time.toLong()
    )
}

fun ChatModel.toRecentMessageEntity() : RecentMessageEntity {
    return RecentMessageEntity(
        messageId = this.messageId!!,
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
        messageId = this.messageId,
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
        messageId = this.messageId!!,
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}

fun MessageEntity.toChatModel() : ChatModel {
    return ChatModel(
        messageId = this.messageId,
        senderId = this.senderId,
        receiverId = this.receiverId,
        message = this.message,
        time = this.time,
        date = this.date
    )
}
