package com.maisel.domain.user.usecase

import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<List<User>> {
        return userRepository.getUsers()
    }
}
