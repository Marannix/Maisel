package com.maisel.domain.database.usecase

import com.maisel.domain.database.repository.ApplicationCacheRepository
import javax.inject.Inject

class UpdateShowcaseStatusUseCase @Inject constructor(private val applicationCacheRepository: ApplicationCacheRepository){
    suspend operator fun invoke(hasSeenShowcase: Boolean) {
        return applicationCacheRepository.updateShowcase(hasSeenShowcase)
    }
}
