package com.maisel.repository

interface UserRepositoryImpl {

    fun createAccount(email: String, password: String)
}