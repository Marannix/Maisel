package com.maisel.signup

data class SignUpValidator(val showEmailError: Boolean = false,
                           val showPasswordError: Boolean = false,
                           val showNameError: Boolean = false)
