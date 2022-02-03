package com.maisel.signin

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
    val focusRequester: FocusRequester,
    val localFocusRequester: FocusManager
)

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
