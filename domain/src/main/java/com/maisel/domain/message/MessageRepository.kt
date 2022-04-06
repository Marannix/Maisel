package com.maisel.domain.message

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun startListeningToMessages(senderId: String, receiverId: String)

    fun stopListeningToMessages(senderId: String, receiverId: String)

    fun stopListeningToSendMessages(senderRoom: String)

    fun observeListOfMessages(): Observable<List<MessageModel>>

    fun getSenderUid(): String?

    fun sendMessage(input: String, senderUid: String, receiverId: String, model: MessageModel)

    fun observeLastMessage(): Observable<String>

    fun fetchLastMessage(userId: String): Flow<Result<String>>

    fun listenToRecentMessages(): Flow<Result<List<MessageModel>>>

    suspend fun insertRecentMessages(messages: List<MessageModel>)

    suspend fun getRecentMessages(): Flow<List<MessageModel>>
}
