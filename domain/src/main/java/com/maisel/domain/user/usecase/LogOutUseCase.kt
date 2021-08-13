package com.maisel.domain.user.usecase

import com.maisel.domain.user.repository.UserRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val userRepository: UserRepository){
    operator fun invoke() {
        userRepository.logoutUser()
    }
}