package com.maisel.data.database

import androidx.datastore.core.DataStore
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.ApplicationSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.SerializationException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TODO: Create usecase for functions within this class
 */
@Singleton
class ApplicationCache @Inject constructor(private val dataStore: DataStore<ApplicationSetting>){

    private val _cacheState = MutableStateFlow<ApplicationCacheState>(ApplicationCacheState.Loading)
    var state = _cacheState.asStateFlow()

    suspend fun loadInCache() {
        try {
            dataStore.data.collectLatest {
                _cacheState.value = ApplicationCacheState.Loaded(it)
            }
        } catch(error: SerializationException) {
            _cacheState.value = ApplicationCacheState.Error
        }
    }

    suspend fun updateShowcase(hasSeenShowcase: Boolean) {
        dataStore.updateData { actualSettings: ApplicationSetting ->
            actualSettings.copy(hasSeenShowcase = true)
        }
    }
}
