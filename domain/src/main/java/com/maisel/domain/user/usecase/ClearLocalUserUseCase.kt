package com.maisel.domain.user.usecase

import com.maisel.domain.database.repository.ApplicationCacheRepository
import javax.inject.Inject

class ClearLocalUserUseCase @Inject constructor(
    private val applicationCacheRepository: ApplicationCacheRepository
) {
    suspend operator fun invoke() {
        return applicationCacheRepository.clearUser()
    }

}
