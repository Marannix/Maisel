package com.maisel.domain.user.usecase

import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class FetchListOfUsersUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        return userRepository.fetchListOfUsers().collectLatest { result ->
            userRepository.insertUsers(result.getOrThrow())
        }
    }
}
