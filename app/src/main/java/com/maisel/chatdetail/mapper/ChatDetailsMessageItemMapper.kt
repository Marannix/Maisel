package com.maisel.chatdetail.mapper

import com.maisel.chatdetail.MessageItem
import com.maisel.domain.message.ChatModel
import javax.inject.Inject

class ChatDetailsMessageItemMapper @Inject constructor() {

    operator fun invoke(
        messages: List<ChatModel>,
        senderUid: String
    ): List<MessageItem> {
        val listOfMessagesItem = mutableListOf<MessageItem>()
        messages.forEach { model ->
                if (model.senderId == senderUid) {
                    listOfMessagesItem.add(
                        MessageItem.SenderMessageItem(
                            model.senderId,
                            model.message,
                            model.time,
                            model.date
                        )
                    )
                } else {
                    listOfMessagesItem.add(
                        MessageItem.ReceiverMessageItem(
                            model.senderId,
                            model.message,
                            model.time,
                            model.date
                        )
                    )
                }
            }

        return listOfMessagesItem
    }
}