package com.maisel.signup

import com.maisel.common.BaseViewModel
import com.maisel.common.state.ValidationError
import com.maisel.compose.state.onboarding.compose.AuthenticationFormState
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

    val input: StateFlow<AuthenticationFormState> = signUpComposerController.input

    val validationErrors: StateFlow<ValidationError.AuthenticationError> =
        signUpComposerController.validationErrors

    fun onSignUpClicked(authenticationFormState: AuthenticationFormState) {
        signUpComposerController.makeSignUpRequest(authenticationFormState)
    }

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
    fun setSignUpInput(value: AuthenticationFormState): Unit =
        signUpComposerController.setSignUpInput(value)
}
