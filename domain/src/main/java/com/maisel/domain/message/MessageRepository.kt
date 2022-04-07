package com.maisel.domain.message

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun stopListeningToSendMessages(senderRoom: String)

    fun getSenderUid(): String?

    fun sendMessage(input: String, senderUid: String, receiverId: String, model: MessageModel)

    fun fetchLastMessage(userId: String): Flow<Result<String>>

    fun listenToRecentMessages(): Flow<Result<List<MessageModel>>>

    fun listenToMessages(senderId: String, receiverId: String): Flow<Result<List<MessageModel>>>

    suspend fun insertRecentMessages(messages: List<MessageModel>)

    suspend fun getRecentMessages(): Flow<List<MessageModel>>

    suspend fun insertMessages(messages: List<MessageModel>)

    suspend fun getListOfMessages(): Observable<List<MessageModel>>

}
