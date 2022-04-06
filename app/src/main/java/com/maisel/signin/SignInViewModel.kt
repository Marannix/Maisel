package com.maisel.signin

import com.google.firebase.auth.AuthCredential
import com.maisel.common.BaseViewModel
import com.maisel.common.state.ValidationError
import com.maisel.compose.state.onboarding.compose.AuthenticationState
import com.maisel.compose.state.onboarding.compose.SignInComposerController
import com.maisel.domain.user.usecase.GetLoggedInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loggedInUser: GetLoggedInUser,
    private val signInComposerController: SignInComposerController
) : BaseViewModel() {

    val state: StateFlow<SignInViewState> = signInComposerController.state

    val input: StateFlow<AuthenticationState> = signInComposerController.input

    val validationErrors: StateFlow<ValidationError.AuthenticationError> = signInComposerController.validationErrors

    private fun signInWithEmailAndPassword(authenticationState: AuthenticationState) {
        signInComposerController.makeLoginRequest(authenticationState)
    }

    fun signInWithCredential(credential: AuthCredential) {
        signInComposerController.signInWithCredential(credential)
    }

    fun isUserLoggedIn(): Boolean {
        return loggedInUser.getLoggedInUser() != null
    }

    fun onLoginClicked(authenticationState: AuthenticationState) {
        signInWithEmailAndPassword(authenticationState)
    }

    //TODO: Doesn't work
    fun onLongPressed() {
        signInWithEmailAndPassword(AuthenticationState("laptop@admin.com", "Password2"))
    }

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
     fun setSignInInput(value: AuthenticationState): Unit = signInComposerController.setSignInInput(value)

    /**
     * Disposes the inner [SignInComposerController].
     */
    override fun onCleared() {
        super.onCleared()
        signInComposerController.onCleared()
    }
}
