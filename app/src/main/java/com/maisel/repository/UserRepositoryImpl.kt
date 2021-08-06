package com.maisel.repository

import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

interface UserRepository {
    fun createAccount(name: String, email: String, password: String) : Maybe<AuthResult>
}