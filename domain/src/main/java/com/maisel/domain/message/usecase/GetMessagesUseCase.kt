package com.maisel.domain.message.usecase

import com.maisel.domain.message.ChatModel
import com.maisel.domain.message.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository){

    operator fun invoke(senderId: String, receiverId: String): Flow<Result<List<ChatModel>>> {
        return messageRepository.listenToChatMessages(senderId, receiverId)
    }

    sealed class MessageDataState {
        object Loading: MessageDataState()
        object Empty: MessageDataState()
        data class Success(val messages: List<ChatModel>): MessageDataState()
        object Error : MessageDataState()
    }
}
