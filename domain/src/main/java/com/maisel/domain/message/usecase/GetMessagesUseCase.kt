package com.maisel.domain.message.usecase

import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository){

    operator fun invoke(senderId: String, receiverId: String): Flow<Result<List<MessageModel>>> {
        return messageRepository.listenToMessages(senderId, receiverId)
    }

    sealed class MessageDataState {
        object Loading: MessageDataState()
        object Empty: MessageDataState()
        data class Success(val messages: List<MessageModel>): MessageDataState()
        object Error : MessageDataState()
    }
}
