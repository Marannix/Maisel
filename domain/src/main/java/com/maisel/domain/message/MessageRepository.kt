package com.maisel.domain.message

import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun stopListeningToSendMessages(senderRoom: String)

    fun getSenderUid(): String?

    fun sendMessage(input: String, senderUid: String, receiverId: String, model: ChatDataModel)

    fun fetchLastMessage(userId: String): Flow<Result<String>>

    fun listenToRecentMessages(): Flow<Result<List<ChatModel>>>

    fun listenToChatMessages(senderId: String, receiverId: String): Flow<Result<List<ChatModel>>>

    suspend fun insertRecentMessages(messages: List<ChatModel>)

    suspend fun getRecentMessages(): Flow<List<ChatModel>>

    suspend fun insertMessages(messages: List<ChatModel>)

    suspend fun getListOfChatMessages(): Flow<List<ChatModel>>
}
