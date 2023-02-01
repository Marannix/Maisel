package com.maisel.utils

enum class FieldValidationResult {
    EMPTY, INVALID, VALID;

    val isFieldValid: Boolean
        get() = this == VALID
}
