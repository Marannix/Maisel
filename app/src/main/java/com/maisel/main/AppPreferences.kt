package com.maisel.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.maisel.main.AppPreferences.Constants.KEY_HAS_SEEN_SHOWCASE
import com.maisel.main.AppPreferences.Constants.KEY_USER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val hasSeenShowcasePreferencesKey = booleanPreferencesKey(KEY_HAS_SEEN_SHOWCASE)
    private val userPreferencesKey = booleanPreferencesKey(KEY_USER)

    val hasSeenShowcase: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[hasSeenShowcasePreferencesKey] ?: false
        }

    suspend fun setShowcase(newValue: Boolean) {
        dataStore.edit { preferences ->
            preferences[hasSeenShowcasePreferencesKey] = newValue
        }
    }

    object Constants {
        const val KEY_HAS_SEEN_SHOWCASE = "HAS_SEEN_SHOWCASE"
        const val KEY_USER = "KEY_USER"
    }
}
