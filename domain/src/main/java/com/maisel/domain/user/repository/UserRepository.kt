package com.maisel.domain.user.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Maybe

interface UserRepository {
    fun createAccount(name: String, email: String, password: String) : Maybe<AuthResult>
    fun signInWithEmailAndPassword(email: String, password: String) : Maybe<AuthResult>
    fun getCurrentUser() : FirebaseUser?
    fun logoutUser()
}