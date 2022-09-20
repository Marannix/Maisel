package com.maisel.domain.user.usecase

import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipientUserUseCase @Inject constructor(private val userRepository: UserRepository){
    operator fun invoke(userId: String): Flow<User> {
        return userRepository.getRecipientUser(userId)
    }
}
