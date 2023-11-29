package com.maisel.data.database

import androidx.datastore.core.DataStore
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.ApplicationSetting
import com.maisel.domain.database.repository.ApplicationCacheRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.SerializationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationCacheRepositoryImpl @Inject constructor(private val dataStore: DataStore<ApplicationSetting>) :
    ApplicationCacheRepository {

    private val _cacheState = MutableStateFlow<ApplicationCacheState>(ApplicationCacheState.Loading)
    var state = _cacheState.asStateFlow()

    override suspend fun loadApplicationCache() {
        try {
            dataStore.data.collectLatest {
                _cacheState.value = ApplicationCacheState.Loaded(it)
            }
        } catch (error: SerializationException) {
            _cacheState.value = ApplicationCacheState.Error
        }
    }

    override suspend fun updateShowcase(hasSeenShowcase: Boolean) {
        dataStore.updateData { actualSettings: ApplicationSetting ->
            actualSettings.copy(hasSeenShowcase = hasSeenShowcase)
        }
    }

    override suspend fun updateLoggedInStatus(isLoggedIn: Boolean) {
        dataStore.updateData { actualSettings: ApplicationSetting ->
            actualSettings.copy(isLoggedIn = isLoggedIn)
        }
    }

    override fun getCacheState(): StateFlow<ApplicationCacheState> {
        return state
    }
}
