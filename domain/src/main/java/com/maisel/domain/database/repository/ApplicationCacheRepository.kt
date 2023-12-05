package com.maisel.domain.database.repository

import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.user.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ApplicationCacheRepository {
    suspend fun updateShowcase(hasSeenShowcase: Boolean)
    suspend fun updateUser(auth: User)
    suspend fun updateLoggedInStatus(isLoggedIn: Boolean)
    suspend fun loadApplicationCache()
    fun getCacheState(): Flow<ApplicationCacheState>
}
