package com.maisel.domain.user.usecase

import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import javax.inject.Inject

class GetLoggedInUser @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): User? {
        return userRepository.getLoggedInUser()
    }
}
