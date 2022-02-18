package com.maisel.domain.message.usecase

import com.maisel.domain.message.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastMessageUseCase @Inject constructor(private val messageRepository: MessageRepository){
    operator fun invoke(userId: String): Flow<Result<String>> {
        return messageRepository.fetchLastMessage(userId)
    }
}
