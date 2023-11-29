package com.maisel.domain.database.usecase

import com.maisel.domain.database.repository.ApplicationCacheRepository
import javax.inject.Inject

class LoadApplicationCacheUseCase @Inject constructor(private val applicationCacheRepository: ApplicationCacheRepository){
    suspend operator fun invoke() {
        return applicationCacheRepository.loadApplicationCache()
    }
}
