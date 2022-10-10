package com.maisel.signin

import com.maisel.state.AuthResultState

//TODO: Name to something more generic for both signin and signup
data class SignInViewState(val authResultState: AuthResultState? = null)
