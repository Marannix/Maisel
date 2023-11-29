package com.maisel.domain.database.repository

import com.maisel.domain.database.ApplicationCacheState
import kotlinx.coroutines.flow.StateFlow

interface ApplicationCacheRepository {
    suspend fun updateShowcase(hasSeenShowcase: Boolean)
    suspend fun updateLoggedInStatus(isLoggedIn: Boolean)
    suspend fun loadApplicationCache()
    fun getCacheState(): StateFlow<ApplicationCacheState>
}
