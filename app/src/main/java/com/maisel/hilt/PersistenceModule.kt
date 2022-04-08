package com.maisel.hilt

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.maisel.data.database.LocalPersistenceManager
import com.maisel.data.database.LocalPersistenceManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class PersistenceModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("maisel", Context.MODE_PRIVATE)
    }

    @Provides
    internal fun provideLocalPersistenceManager(preference: SharedPreferences, gson: Gson) : LocalPersistenceManager {
        return LocalPersistenceManagerImpl(preference, gson)
    }
}
