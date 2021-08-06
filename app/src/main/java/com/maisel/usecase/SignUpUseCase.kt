package com.maisel.usecase

import com.google.firebase.auth.AuthResult
import com.maisel.repository.UserRepository
import io.reactivex.Maybe
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(email: String, name: String, password: String): Maybe<AuthResult> {
        return userRepository.createAccount(email, name, password)
    //.startWith(AuthResultState.Loading)
            //.map {
           //     it.toDomain()
     //   }
            //.startWith(AuthResultState.Loading)
    }
}