package com.maisel.chat

import com.maisel.chat.composables.MessageItem
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.SignUpUser

data class ChatDetailViewState(val user: SignUpUser? = null, val messageItemState: GetMessagesUseCase.MessageDataState? = null, val senderUid: String? = null) {

    fun getMessagesItem(): List<MessageItem> {
        val listOfMessagesItem = mutableListOf<MessageItem>()
        if (messageItemState is GetMessagesUseCase.MessageDataState.Success && senderUid != null) {
            messageItemState.messages.forEach {
                if (it.uid == senderUid) {
                    listOfMessagesItem.add(MessageItem.SenderMessageItem(it.uid, it.message, it.timestamp))
                } else {
                    listOfMessagesItem.add(MessageItem.ReceiverMessageItem(it.uid, it.message, it.timestamp))
                }
            }
        }

        return listOfMessagesItem
    }
}