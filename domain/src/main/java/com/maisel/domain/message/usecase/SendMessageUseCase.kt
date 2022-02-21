package com.maisel.domain.message.usecase

import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(val messageRepository: MessageRepository) {

    operator fun invoke(input: String, senderRoom: String, receiverRoom: String, receiverId: String, model: MessageModel) {
        messageRepository.sendMessage(input, senderRoom, receiverRoom, receiverId, model)
    }
}
