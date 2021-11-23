package com.maisel.data.signup.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.maisel.domain.user.repository.UserRepository
import io.mockk.mockk
import org.junit.Before

class UserRepositoryImplTest {

    lateinit var repository: UserRepository
    private val firebaseAuth: FirebaseAuth = mockk()
    private val database: DatabaseReference = mockk()
    private val authResult: AuthResult = mockk()

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(firebaseAuth, database)
    }

}