package com.maisel.compose.state.onboarding.compose

import androidx.compose.runtime.MutableState
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue

/**
 * Represents the state within the sign in screen
 *
 * @param validationState Validation errors
 * @param showErrorBanner Show error banner
 * @param emailInputState The current text value that's within the input.
 * @param passwordInputValue The current text value that's within the input.
 */
data class SignInState(
    val validationState: ValidationState = ValidationState(),
    val showErrorBanner: Boolean = false,
    val emailInputState: MutableState<TextFieldValue>,
    val passwordInputValue: MutableState<TextFieldValue>,
    val signInForm: SignInForm = SignInForm(),
    val focusRequester: FocusRequester,
    val localFocusRequester: FocusManager
)

/**
 * Represents the sign up form state within the sign up screen
 *
 * @param email The users account creation email address
 * @param password The users account creation password
 */
data class SignInForm(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)
