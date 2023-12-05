package com.maisel.domain.user.usecase

import com.maisel.domain.room.ClearRoomDatabaseUseCase
import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val clearLocalUserUseCase: ClearLocalUserUseCase,
    private val clearRoomDatabaseUseCase: ClearRoomDatabaseUseCase,
) {
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return userRepository.logoutUser().also {
            clearLocalUserUseCase.invoke()
            clearRoomDatabaseUseCase.invoke()
        }
    }
}
