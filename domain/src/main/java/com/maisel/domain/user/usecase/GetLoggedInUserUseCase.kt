package com.maisel.domain.user.usecase

import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoggedInUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<Result<User>> {
        return userRepository.listenToLoggedInUser()
    }

    fun getLoggedInUser() : User? {
        return userRepository.getLoggedInUser()
    }
}
