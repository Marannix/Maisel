package com.maisel.hilt

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.maisel.data.database.ApplicationCacheRepositoryImpl
import com.maisel.data.database.LocalPersistenceManager
import com.maisel.data.database.settingsDataStore
import com.maisel.data.message.dao.MessageDao
import com.maisel.data.message.dao.RecentMessageDao
import com.maisel.data.message.repository.MessageRepositoryImpl
import com.maisel.data.user.dao.UserDao
import com.maisel.data.user.repository.UserRepositoryImpl
import com.maisel.domain.database.repository.ApplicationCacheRepository
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.user.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    //@Singleton
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        databaseReference: DatabaseReference,
        localPersistenceManager: LocalPersistenceManager,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(firebaseAuth, databaseReference, localPersistenceManager, userDao)
    }

    @Provides
  //  @Singleton
    fun provideMessageRepository(
        firebaseAuth: FirebaseAuth,
        databaseReference: DatabaseReference,
        messageDao: MessageDao,
        recentMessageDao: RecentMessageDao
    ): MessageRepository {
        return MessageRepositoryImpl(firebaseAuth, databaseReference, messageDao, recentMessageDao)
    }

    @Provides
    @Singleton
    fun provideApplicationCacheRepository(@ApplicationContext context: Context) : ApplicationCacheRepository {
        return ApplicationCacheRepositoryImpl(context.settingsDataStore)
    }
}
