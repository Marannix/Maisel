package com.maisel.hilt

import android.app.Application
import androidx.room.Room
import com.maisel.data.database.ApplicationDatabase
import com.maisel.data.message.dao.MessageDao
import com.maisel.data.message.dao.RecentMessageDao
import com.maisel.data.user.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(application: Application): ApplicationDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            ApplicationDatabase::class.java, "maisel.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(appDatabase: ApplicationDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideRecentMessageDao(appDatabase: ApplicationDatabase): RecentMessageDao {
        return appDatabase.recentMessageDao()
    }

    @Provides
    fun provideMessageDao(appDatabase: ApplicationDatabase): MessageDao {
        return appDatabase.messageDao()
    }
}
