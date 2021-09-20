package com.maisel.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirebaseModule {
    @Provides
    fun provideFirebase(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    //TODO: Extract path to constant
    //TODO: Next time don't change region from the default
    // https://stackoverflow.com/questions/44606621/firebase-database-not-working-and-no-response
    @Provides
    fun provideFirebaseDatabase() : DatabaseReference {
      return Firebase.database("https://maisel-32ad9-default-rtdb.europe-west1.firebasedatabase.app/").reference
    }
}