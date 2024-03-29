package com.maisel.domain.user.usecase

import android.util.Log
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetLoggedInUserFromFirebaseUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<Result<User>> {
        return userRepository.listenToLoggedInUser()
    }

    fun getLoggedInUser(): User? {
        return userRepository.getLoggedInUser()
    }
}
