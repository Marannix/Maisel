package com.maisel.domain.user.usecase

import com.google.firebase.auth.FirebaseUser
import com.maisel.domain.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class GetCurrentUserTest {

    private val getCurrentUser: GetCurrentUser = mockk()
    private val userRepository: UserRepository = mockk()
    private val firebaseUser: FirebaseUser = mockk()

    @Test
    fun `WHEN get book returns success THEN success state is returned`() {
        every { userRepository.getCurrentUser() } returns firebaseUser
        every { getCurrentUser.invoke() } returns firebaseUser

        assertEquals(getCurrentUser.invoke(), firebaseUser)
    }

}