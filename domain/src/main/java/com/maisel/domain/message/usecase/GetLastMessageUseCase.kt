package com.maisel.domain.message.usecase

import com.maisel.domain.message.MessageRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetLastMessageUseCase @Inject constructor(private val messageRepository: MessageRepository){
    operator fun invoke(): Observable<String> {
        return messageRepository.observeLastMessage().map {it}
    }

    fun startListeningToMessages(userId: String) {
        messageRepository.startListeningToLastMessages(userId)
    }

//    fun stopListeningToMessages() {
//        messageRepository.stopListeningToLastMessages()
//    }

//    sealed class MessageDataState {
//        object Loading: MessageDataState()
//        data class Success(val messages: List<MessageModel>): MessageDataState()
//        object Error : MessageDataState()
//    }
}
