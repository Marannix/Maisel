package com.maisel.domain.user.usecase

import com.google.firebase.auth.AuthResult
import com.maisel.domain.user.repository.UserRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String): AuthResult {
        return userRepository.makeLoginRequest(email, password).getOrThrow()
    }
}
