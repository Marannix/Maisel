package com.maisel.compose.state.onboarding.compose

import androidx.compose.runtime.MutableState
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import com.maisel.common.state.ValidationError

/**
 * Represents the state within the sign up screen
 *
 * @param validationState Validation errors
 * @param nameInputState The current text value that's within the input.
 * @param emailInputState The current text value that's within the input.
 * @param passwordInputValue The current text value that's within the input.
 */
data class SignUpState(
    val validationState: ValidationError.AuthenticationError,
    val authenticationState: MutableState<AuthenticationState>,
    val focusRequester: FocusRequester,
    val localFocusRequester: FocusManager
)

/**
 * Represents the sign up form state within the sign up screen
 *
 * @param name The users account creation name
 * @param email The users account creation email address
 * @param password The users account creation password
 */
data class SignUpForm(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)
