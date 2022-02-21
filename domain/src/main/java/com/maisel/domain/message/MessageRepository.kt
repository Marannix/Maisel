package com.maisel.domain.message

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun startListeningToMessages(senderRoom: String)

    fun stopListeningToMessages(senderRoom: String)

    fun stopListeningToSendMessages(senderRoom: String)

    fun observeListOfMessages(): Observable<List<MessageModel>>

    fun getSenderUid(): String?

    fun sendMessage(input: String, senderRoom: String, receiverRoom: String, receiverId: String, model: MessageModel)

    fun observeLastMessage(): Observable<String>

    fun fetchLastMessage(userId: String): Flow<Result<String>>

    fun getLatestMessagev2(): Flow<Result<List<MessageModel>>>
}
