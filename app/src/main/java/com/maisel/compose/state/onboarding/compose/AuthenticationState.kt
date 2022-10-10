package com.maisel.compose.state.onboarding.compose

import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import com.maisel.common.state.ValidationError

/**
 * Represents the state within the sign up screen
 *
 * @param validationState Validation errors
 * @param showErrorBanner Show error banner
 */
data class AuthenticationState(
    val validationState: ValidationError.AuthenticationError,
    val showErrorBanner: Boolean = false,
    val authenticationFormState: AuthenticationFormState = AuthenticationFormState(),
    val focusRequester: FocusRequester,
    val localFocusRequester: FocusManager
)
