package com.maisel.hilt

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {
    @Provides
    fun provideApplication(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideGson() : Gson {
        return GsonBuilder().create()
    }
}
