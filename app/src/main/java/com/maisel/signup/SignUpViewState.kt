package com.maisel.signup

import com.maisel.state.AuthResultState

data class SignUpViewState(val authResultState: AuthResultState? = null, val signUpValidator: SignUpValidator = SignUpValidator())
