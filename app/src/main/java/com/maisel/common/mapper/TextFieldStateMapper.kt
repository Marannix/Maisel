package com.maisel.common.mapper

import com.maisel.common.composable.TextFieldState
import com.maisel.utils.FieldValidationResult
import com.maisel.utils.TextFieldValidator
import javax.inject.Inject

class TextFieldStateMapper @Inject constructor(
    private val textFieldValidator: TextFieldValidator,
) {

    fun getNameState(name: String): TextFieldState {
        val nameState = when (textFieldValidator.validateName(name)) {
            FieldValidationResult.EMPTY -> TextFieldState.Invalid(
                name,
                "Yikes! No name provided."
            )

            FieldValidationResult.INVALID -> TextFieldState.Invalid(
                name,
                "Oops! Please enter a name."
            )

            FieldValidationResult.VALID -> TextFieldState.Valid(name)
        }

        return nameState
    }

    fun getEmailState(email: String): TextFieldState {
        val emailState = when (textFieldValidator.validateEmail(email)) {
            FieldValidationResult.EMPTY -> TextFieldState.Invalid(
                email,
                "Yikes! No email address provided."
            )

            FieldValidationResult.INVALID -> TextFieldState.Invalid(
                email,
                "Oops! Please enter a valid email address."
            )

            FieldValidationResult.VALID -> TextFieldState.Valid(email)
        }

        return emailState
    }

    fun getPasswordState(password: String): TextFieldState {
        val passwordState = when (textFieldValidator.validatePassword(password)) {
            FieldValidationResult.EMPTY -> TextFieldState.Invalid(
                password,
                "Oops! I think you forgot to enter a password"
            )
            FieldValidationResult.INVALID -> TextFieldState.Invalid(
                password,
                "Oops! Please enter a valid password"
            )
            FieldValidationResult.VALID -> TextFieldState.Valid(password)
        }

        return passwordState
    }
}
