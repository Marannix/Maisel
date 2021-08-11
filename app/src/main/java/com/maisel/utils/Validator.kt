package com.maisel.utils

import java.util.regex.Pattern

class Validator {

    private val VALID_PASSWORD_REGEX = Pattern.compile("^.{8,}$")
    private val VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[A-Z0-9.-]+\\.[A-Z]{2,64}$", Pattern.CASE_INSENSITIVE)

    fun isEmailValid(email: String): Boolean {
        return VALID_EMAIL_REGEX.matcher(email).find()
    }

    fun isPasswordValid(password: String?): Boolean {
        return VALID_PASSWORD_REGEX.matcher(password).find()
    }
}