package com.maisel.chatdetail

import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.User

data class ChatDetailViewState(
    val recipient: User? = null,
    val messageItemState: GetMessagesUseCase.MessageDataState? = null,
    val senderUid: String? = null
) {

    fun getMessagesItem(): List<MessageItem> {
        val listOfMessagesItem = mutableListOf<MessageItem>()
        if (messageItemState is GetMessagesUseCase.MessageDataState.Success && senderUid != null) {
            messageItemState.messages.forEach { model ->
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
        }

        return listOfMessagesItem
    }
}
