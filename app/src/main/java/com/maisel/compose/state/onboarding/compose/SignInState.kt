package com.maisel.compose.state.onboarding.compose

import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import com.maisel.common.state.ValidationError

/**
 * Represents the state within the sign in screen
 *
 * @param validationState Validation errors
 * @param showErrorBanner Show error banner
 * @param emailInputState The current text value that's within the input.
 * @param passwordInputValue The current text value that's within the input.
 */
data class SignInState(
    val validationState: ValidationError.AuthenticationError,
    val showErrorBanner: Boolean = false,
    val authenticationState: AuthenticationState = AuthenticationState(),
    val focusRequester: FocusRequester,
    val localFocusRequester: FocusManager
)

