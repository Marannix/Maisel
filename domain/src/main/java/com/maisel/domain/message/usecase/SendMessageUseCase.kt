package com.maisel.domain.message.usecase

import com.maisel.domain.message.ChatModel
import com.maisel.domain.message.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(val messageRepository: MessageRepository) {

    operator fun invoke(input: String, senderUid: String, receiverId: String, model: ChatModel) {
        messageRepository.sendMessage(input, senderUid, receiverId, model)
    }
}
