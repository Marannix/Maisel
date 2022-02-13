package com.maisel.compose.state.onboarding.compose

import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.user.usecase.SignInUseCase
import com.maisel.signin.SignInViewState
import com.maisel.state.AuthResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInComposerController @Inject constructor(
    private val signInUseCase: SignInUseCase
) {
    /**
     * Creates a [CoroutineScope] that allows us to cancel the ongoing work when the parent
     * ViewModel is disposed.
     */
    private val scope = CoroutineScope(DispatcherProvider.Main)

    /**
     * UI state of the current composer input.
     */
    private val _stateFlow: MutableStateFlow<SignInViewState> = MutableStateFlow(SignInViewState())

    val state: StateFlow<SignInViewState>
        get() = _stateFlow

    /**
     * UI state of the current composer input.
     */
    val input: MutableStateFlow<SignInForm> = MutableStateFlow(SignInForm())

    /**
     * Gets the current name text input in the message composer.
     */
    private val nameText: String
        get() = input.value.name

    /**
     * Gets the current email text input in the message composer.
     */
    private val emailText: String
        get() = input.value.email

    /**
     * Gets the current password text input in the message composer.
     */
    private val passwordText: String
        get() = input.value.password

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
    fun setSignInInput(value: SignInForm) {
        this.input.value = value
        //handleValidationErrors()
    }

    fun makeLoginRequest(value: SignInForm) {
        scope.launch {
            val result = signInUseCase.invoke(value.email, value.password)
            if (result != null && result.user != null) {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Success(result.user!!)) }
            } else {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Error) }
            }
        }
    }
}
