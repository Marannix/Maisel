package com.maisel.compose.state.onboarding.compose

/**
 * Represents the validation state within the sign in screen
 *
 * @param showEmailError The current validation state of error
 * @param showPasswordError The current validation state of password
 */
data class ValidationState(
    val showNameError: Boolean = false,
    val showEmailError: Boolean = false,
    val showPasswordError: Boolean = false
)
