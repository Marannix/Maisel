package com.maisel.compose.state.onboarding.compose

/**
 * Represents the authentication form state within the sign up screen
 *
 * @param email The users account creation email address
 * @param password The users account creation password
 */
//TODO: Rename AuthenticationFormState
class AuthenticationState(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)
