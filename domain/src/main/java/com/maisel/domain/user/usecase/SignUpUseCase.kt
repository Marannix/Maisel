package com.maisel.domain.user.usecase

import com.google.firebase.auth.AuthResult
import com.maisel.domain.user.repository.UserRepository
import io.reactivex.Maybe
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(name: String, email: String, password: String): Maybe<AuthResult> {
        return userRepository.createAccount(name, email, password)
    }
}