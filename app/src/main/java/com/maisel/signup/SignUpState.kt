package com.maisel.signup

import androidx.compose.runtime.MutableState
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import com.maisel.signin.ValidationState

/**
 * Represents the state within the sign up screen
 *
 * @param validationState Validation errors
 * @param nameInputState The current text value that's within the input.
 * @param emailInputState The current text value that's within the input.
 * @param passwordInputValue The current text value that's within the input.
 */
data class SignUpState(
    val validationState: ValidationState = ValidationState(),
    val nameInputState: MutableState<TextFieldValue>,
    val emailInputState: MutableState<TextFieldValue>,
    val passwordInputValue: MutableState<TextFieldValue>,
    val signUpForm: SignUpForm = SignUpForm(),
    val focusRequester: FocusRequester,
    val localFocusRequester: FocusManager
)

///**
// * Represents the validation state within the sign up screen
// *
// * @param showNameError The current validation state of name
// * @param showEmailError The current validation state of error
// * @param showPasswordError The current validation state of password
// */
//data class ValidationState(
//    val showNameError: Boolean = false,
//    val showEmailError: Boolean = false,
//    val showPasswordError: Boolean = false
//)

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
