package com.maisel.domain.message.usecase

import com.maisel.domain.message.MessageRepository
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetLastMessageUseCase @Inject constructor(private val messageRepository: MessageRepository){
    suspend operator fun invoke() {
        return messageRepository.listenToRecentMessages().collectLatest{ result ->
            messageRepository.insertRecentMessages(result.getOrThrow())
        }
    }
}
