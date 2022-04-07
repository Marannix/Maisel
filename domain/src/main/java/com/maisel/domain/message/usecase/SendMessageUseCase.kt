package com.maisel.domain.message.usecase

import com.maisel.domain.message.ChatDataModel
import com.maisel.domain.message.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(val messageRepository: MessageRepository) {

    operator fun invoke(input: String, senderUid: String, receiverId: String, model: ChatDataModel) {
        messageRepository.sendMessage(input, senderUid, receiverId, model)
    }
}
