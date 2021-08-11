package com.maisel.state

import com.maisel.signup.SignUpValidator

data class SignUpViewState(val authResultState: AuthResultState? = null, val signUpValidator: SignUpValidator = SignUpValidator())