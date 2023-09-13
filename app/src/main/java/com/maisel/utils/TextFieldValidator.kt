package com.maisel.utils

import java.util.regex.Pattern
import javax.inject.Inject

class TextFieldValidator @Inject constructor() {

    /**
     *  @param name Name to be validated
     *  Return [FieldValidationResult] based on validation
     */
    fun isNameValid(name: String): FieldValidationResult {
        return when {
            name.isEmpty() -> FieldValidationResult.EMPTY
            else -> FieldValidationResult.VALID
        }
    }

    /**
     *  @param email Email address to be validated
     *  Return [FieldValidationResult] based on validation
     */
    fun validateEmail(email: String): FieldValidationResult {
        return when {
            email.isEmpty() -> FieldValidationResult.EMPTY
            !VALID_EMAIL_REGEX.matcher(email).find() -> FieldValidationResult.INVALID
            else -> FieldValidationResult.VALID
        }
    }

    /**
     *  @param password Password to be validated
     *  Used to validate user password during sign in flow
     *  Return [FieldValidationResult]
     */
    fun validatePassword(password: String?): FieldValidationResult {
        return when {
            password.isNullOrEmpty() -> FieldValidationResult.EMPTY
            else -> FieldValidationResult.VALID
        }
    }

    /**
     *  @param password Password to be validated
     *  Used to validate user password during sign up flow
     *  Return [FieldValidationResult]
     */
    fun validateCreatePassword(password: String?): FieldValidationResult {
        return when {
            password.isNullOrEmpty() -> FieldValidationResult.EMPTY
            !VALID_PASSWORD_REGEX.matcher(password).find() -> FieldValidationResult.INVALID
            else -> FieldValidationResult.VALID
        }
    }

    companion object {
        private val VALID_PASSWORD_REGEX = Pattern.compile("^.{8,}$")
        private val VALID_EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[A-Z0-9.-]+\\.[A-Z]{2,64}$",
            Pattern.CASE_INSENSITIVE
        )
    }
}
