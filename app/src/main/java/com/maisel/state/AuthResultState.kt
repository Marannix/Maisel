package com.maisel.state

sealed class AuthResultState {
    object Loading : AuthResultState()
    object Success : AuthResultState()
    object Error : AuthResultState()
    object Idle : AuthResultState()
}