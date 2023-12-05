package com.maisel.domain.user.usecase

import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class GetLoggedInUserTest {

    private val getLoggedInUser: GetLoggedInUserFromFirebaseUseCase = mockk()
    private val userRepository: UserRepository = mockk()
    private val user: User = mockk()

    @Test
    fun `WHEN get logged in user returns success THEN success state is returned`() {
        every { userRepository.getLoggedInUser() } returns user
        every { getLoggedInUser.getLoggedInUser() } returns user

        assertEquals(getLoggedInUser.getLoggedInUser(), user)
    }
}
