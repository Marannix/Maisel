package com.maisel.domain.user.usecase

import com.google.firebase.auth.FirebaseUser
import com.maisel.domain.user.repository.UserRepository
import javax.inject.Inject

class GetCurrentUser @Inject constructor(val userRepository: UserRepository) {
    operator fun invoke(): FirebaseUser? {
        return userRepository.getFirebaseCurrentUser()
    }
}
