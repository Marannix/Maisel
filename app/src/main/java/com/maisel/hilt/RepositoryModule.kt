package com.maisel.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.maisel.data.coroutine.DispatcherProvider
import com.maisel.data.message.repository.MessageRepositoryImpl
import com.maisel.data.signup.repository.UserRepositoryImpl
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.user.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuth: FirebaseAuth, databaseReference: DatabaseReference): UserRepository {
        return UserRepositoryImpl(firebaseAuth, databaseReference)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(firebaseAuth: FirebaseAuth, databaseReference: DatabaseReference): MessageRepository {
        return MessageRepositoryImpl(firebaseAuth, databaseReference)
    }
}
