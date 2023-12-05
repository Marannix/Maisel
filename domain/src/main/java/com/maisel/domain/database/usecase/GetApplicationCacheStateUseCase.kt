package com.maisel.domain.database.usecase

import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.repository.ApplicationCacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetApplicationCacheStateUseCase @Inject constructor(private val applicationCacheRepository: ApplicationCacheRepository){
    operator fun invoke(): Flow<ApplicationCacheState> {
        return applicationCacheRepository.getCacheState()
    }
}
