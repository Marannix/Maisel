package com.maisel.state

import com.google.firebase.auth.FirebaseUser

sealed class AuthResultState {

    object Loading : AuthResultState()
    data class Success(val user: FirebaseUser): AuthResultState()
    object Error : AuthResultState()
    object Idle : AuthResultState()
}