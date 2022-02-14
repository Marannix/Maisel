package com.maisel.common.state

/**
 * Represents a validation error for the user input.
 */
sealed class ValidationError {

    /**
     * TODO:
     */
    data class AuthenticationError (
        val nameError: Boolean = false,
        val emailError: Boolean = false,
        val passwordError: Boolean = false,
    ) : ValidationError()
}
