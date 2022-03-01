package com.maisel.domain.user.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.maisel.domain.user.entity.User
import io.reactivex.Maybe
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * Creates an account to firebase using name, email and password.
     * @return an AuthResult which is either a success or failure.
     */
    fun createAccount(name: String, email: String, password: String) : Maybe<AuthResult>

    /**
     * Sign in using email and password.
     * @return an AuthResult which is either a success or failure.
     */
    suspend fun makeLoginRequest(email: String, password: String): AuthResult?

    /**
     * Gets current user logged in
     * @return either a user if successful or null
     */
    fun getFirebaseCurrentUser() : FirebaseUser?

    suspend fun signInWithCredential(credential: AuthCredential): AuthResult?

    fun setCurrentUser(firebaseUser: FirebaseUser)

    fun logoutUser()

    fun observeListOfUsers(): Observable<List<User>>

    fun getSenderUid(): String?

    fun fetchListOfUsers(): Flow<Result<List<User>>>

    fun getCurrentUser(): Flow<Result<User>>
}
