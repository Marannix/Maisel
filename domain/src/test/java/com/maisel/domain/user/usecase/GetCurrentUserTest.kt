package com.maisel.domain.user.usecase

import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class GetCurrentUserTest {

    private val getCurrentUser: GetCurrentUser = mockk()
    private val userRepository: UserRepository = mockk()
    private val user: User = mockk()

    @Test
    fun `WHEN get book returns success THEN success state is returned`() {
        every { userRepository.getLoggedInUser() } returns user
        every { getCurrentUser.invoke() } returns user

        assertEquals(getCurrentUser.invoke(), user)
    }
}
