package com.maisel.domain.user.usecase

import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val userRepository: UserRepository){
    operator fun invoke(): Flow<Result<Unit>> {
        return userRepository.logoutUser()
    }
}
