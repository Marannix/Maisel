package com.maisel.domain.user.usecase

import com.google.firebase.auth.AuthResult
import com.maisel.domain.user.repository.UserRepository
import io.reactivex.Maybe
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(email: String, password: String): Maybe<AuthResult> {
        return userRepository.signInWithEmailAndPassword(email, password)
    }

    suspend fun makeLoginRequest(email: String, password: String): AuthResult? {
        return userRepository.makeLoginRequest(email, password)
    }
}
