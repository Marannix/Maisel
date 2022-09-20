package com.maisel.signup

import com.maisel.common.BaseViewModel
import com.maisel.common.state.ValidationError
import com.maisel.compose.state.onboarding.compose.AuthenticationState
import com.maisel.compose.state.onboarding.compose.SignUpComposerController
import com.maisel.signin.SignInViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpComposerController: SignUpComposerController
) : BaseViewModel() {

    val state: StateFlow<SignInViewState> = signUpComposerController.state

    val input: StateFlow<AuthenticationState> = signUpComposerController.input

    val validationErrors: StateFlow<ValidationError.AuthenticationError> =
        signUpComposerController.validationErrors

    fun onSignUpClicked(authenticationState: AuthenticationState) {
        signUpComposerController.makeSignUpRequest(authenticationState)
    }

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
    fun setSignUpInput(value: AuthenticationState): Unit =
        signUpComposerController.setSignUpInput(value)
}
