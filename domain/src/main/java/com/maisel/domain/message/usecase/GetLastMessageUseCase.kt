package com.maisel.domain.message.usecase

import com.maisel.domain.message.ChatModel
import com.maisel.domain.message.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//TODO: Delete or move usecase?
class GetLastMessageUseCase @Inject constructor(private val messageRepository: MessageRepository){
    operator fun invoke(): Flow<Result<List<ChatModel>>> {
        return messageRepository.listenToRecentMessages()
    }
}
