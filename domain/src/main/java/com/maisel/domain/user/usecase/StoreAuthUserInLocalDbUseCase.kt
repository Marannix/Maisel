package com.maisel.domain.user.usecase

import com.maisel.domain.database.repository.ApplicationCacheRepository
import com.maisel.domain.user.entity.User
import javax.inject.Inject

class StoreAuthUserInLocalDbUseCase @Inject constructor(
    private val applicationCacheRepository: ApplicationCacheRepository
) {
    suspend fun invoke(user: User) {
        return applicationCacheRepository.updateUser(user)
    }
}
