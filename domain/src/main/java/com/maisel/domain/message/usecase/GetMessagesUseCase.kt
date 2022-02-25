package com.maisel.domain.message.usecase

import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository){
    operator fun invoke(): Observable<MessageDataState> {
        return messageRepository.observeListOfMessages().map<MessageDataState> {
            MessageDataState.Success(it)
        }.onErrorReturn {
            MessageDataState.Error
        }
    }

    fun startListeningToMessages(senderId: String, receiverId: String) {
        messageRepository.startListeningToMessages(senderId, receiverId)
    }

    fun stopListeningToMessages(senderId: String, receiverId: String) {
        messageRepository.stopListeningToMessages(senderId, receiverId)
    }

    sealed class MessageDataState {
        object Loading: MessageDataState()
        data class Success(val messages: List<MessageModel>): MessageDataState()
        object Error : MessageDataState()
    }
}
