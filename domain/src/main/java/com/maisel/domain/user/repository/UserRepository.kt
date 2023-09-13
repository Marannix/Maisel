package com.maisel.domain.user.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.maisel.domain.user.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * Creates an account to firebase using name, email and password.
     * @return an AuthResult which is either a success or failure.
     */
    suspend fun createAccount(name: String, email: String, password: String): AuthResult?

    /**
     * Sign in using email and password.
     * @return an AuthResult which is either a success or failure.
     */
    suspend fun makeLoginRequest(email: String, password: String): Result<AuthResult>

    suspend fun signInWithCredential(credential: AuthCredential): AuthResult?

    /**
     * Gets current user logged in
     * @return either a user if successful or null
     */
    fun getLoggedInUser(): User?

    fun logoutUser(): Flow<Result<Unit>>

    fun fetchListOfUsers(): Flow<Result<List<User>>>

    fun listenToLoggedInUser(): Flow<Result<User>>

    suspend fun getUsers(): Flow<List<User>>

    suspend fun insertUsers(users: List<User>)

    fun deleteAllUsers()

    fun getRecipientUser(userId: String): Flow<User>
}
