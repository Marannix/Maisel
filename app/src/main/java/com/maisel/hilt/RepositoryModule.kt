package com.maisel.hilt

import com.google.firebase.auth.FirebaseAuth
import com.maisel.repository.UserRepository
import com.maisel.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    fun provideUserRepository(firebaseAuth: FirebaseAuth): UserRepository {
        return UserRepositoryImpl(firebaseAuth)
    }
}