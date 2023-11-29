package com.maisel.domain.user.usecase

import com.maisel.domain.message.ChatModel
import com.maisel.domain.message.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository){
    suspend operator fun invoke(): Flow<List<ChatModel>> {
        return messageRepository.getRecentMessages()
    }
}
