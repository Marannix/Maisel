package com.maisel.domain.user.repository

import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

interface UserRepository {
    fun createAccount(name: String, email: String, password: String) : Maybe<AuthResult>
    fun signInWithEmailAndPassword(email: String, password: String) : Maybe<AuthResult>
}