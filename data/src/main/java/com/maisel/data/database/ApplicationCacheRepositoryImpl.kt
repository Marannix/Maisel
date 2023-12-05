package com.maisel.data.database

import android.util.Log
import androidx.datastore.core.DataStore
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.ApplicationSetting
import com.maisel.domain.database.repository.ApplicationCacheRepository
import com.maisel.domain.user.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationCacheRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<ApplicationSetting>,
    @Singleton private val applicationCoroutineScope: CoroutineScope
) :
    ApplicationCacheRepository {

    private val _cacheState = MutableStateFlow<ApplicationCacheState>(ApplicationCacheState.Loading)
    val state = _cacheState.asStateFlow()

    init {
        applicationCoroutineScope.launch {
            try {
                dataStore.data.collectLatest {
                    Log.d("joshua appcache:", it.toString())
                    _cacheState.value = ApplicationCacheState.Loaded(it)
                }
            } catch (error: SerializationException) {
                Log.d("joshua appcacheError:", error.toString())
                _cacheState.value = ApplicationCacheState.Error
            }
        }
    }

    override suspend fun loadApplicationCache() {

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

    override suspend fun updateUser(user: User) {
        val x = dataStore.updateData { actualSettings: ApplicationSetting ->
            actualSettings.copy(user = user)
        }


    }

    override fun getCacheState(): Flow<ApplicationCacheState> {
        return state
    }
}
