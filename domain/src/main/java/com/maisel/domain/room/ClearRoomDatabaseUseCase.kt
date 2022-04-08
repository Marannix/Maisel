package com.maisel.domain.room

import com.maisel.domain.message.MessageRepository
import com.maisel.domain.user.repository.UserRepository
import javax.inject.Inject

class ClearRoomDatabaseUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) {
    operator fun invoke() {
        userRepository.deleteAllUsers()
        messageRepository.deleteAllMessages()
        messageRepository.deleteAllRecentMessages()
    }
}
