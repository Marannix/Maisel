package com.maisel.domain.user.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Maybe

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
    fun signInWithEmailAndPassword(email: String, password: String) : Maybe<AuthResult>

    /**
     * Gets current user logged in
     * @return either a user if successful or null
     */
    fun getCurrentUser() : FirebaseUser?

    fun signInWithCredential(idToken: String, credential: AuthCredential): Maybe<AuthResult>

    fun setCurrentUser(firebaseUser: FirebaseUser)

    fun logoutUser()
}