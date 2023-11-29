package com.maisel.domain.user.usecase

import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FetchListOfUsersUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        return userRepository.fetchListOfUsers().collect {result ->
            if (result.isSuccess) {
                userRepository.insertUsers(result.getOrThrow())
            }
        }
    }
}
