package com.maisel.domain.user.usecase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.maisel.domain.user.repository.UserRepository
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke(credential: AuthCredential): AuthResult? {
        return userRepository.signInWithCredential(credential)
    }
}
