package com.maisel.repository

interface UserRepositoryImpl {

    fun createAccount(name: String, email: String, password: String)
}