package com.maisel.domain.message

import io.reactivex.Observable

interface MessageRepository {

    fun startListeningToMessages(senderRoom: String)

    fun stopListeningToMessages(senderRoom: String)

    fun stopListeningToSendMessages(senderRoom: String)

    fun observeListOfMessages(): Observable<List<MessageModel>>

    fun getSenderUid(): String?

    fun sendMessage(input: String, senderRoom: String, receiverRoom: String, model: MessageModel)
}
