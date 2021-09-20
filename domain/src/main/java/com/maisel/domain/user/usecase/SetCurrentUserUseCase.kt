package com.maisel.domain.user.usecase

import com.google.firebase.auth.FirebaseUser
import com.maisel.domain.user.repository.UserRepository
import javax.inject.Inject

class SetCurrentUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(firebaseUser: FirebaseUser) {
        return userRepository.setCurrentUser(firebaseUser)
    }
}