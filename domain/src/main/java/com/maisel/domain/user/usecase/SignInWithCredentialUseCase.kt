package com.maisel.domain.user.usecase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.maisel.domain.user.repository.UserRepository
import io.reactivex.Maybe
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(val userRepository: UserRepository) {
    operator fun invoke(idToken: String, credential: AuthCredential): Maybe<AuthResult> {
        return userRepository.signInWithCredential(idToken, credential)
    }
}