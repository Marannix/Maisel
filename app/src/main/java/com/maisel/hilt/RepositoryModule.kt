package com.maisel.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.maisel.data.signup.repository.UserRepositoryImpl
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
}
