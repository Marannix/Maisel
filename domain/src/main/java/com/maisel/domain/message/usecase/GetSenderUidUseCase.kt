package com.maisel.domain.message.usecase

import com.maisel.domain.message.MessageRepository
import javax.inject.Inject

class GetSenderUidUseCase @Inject constructor(
    private val messageRepository: MessageRepository
){

    operator fun invoke() : String? {
        return messageRepository.getSenderUid()
    }
}
