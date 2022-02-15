package com.maisel.compose.state.onboarding.compose

import com.google.firebase.auth.AuthCredential
import com.maisel.common.state.ValidationError
import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.user.usecase.SignInUseCase
import com.maisel.domain.user.usecase.SignInWithCredentialUseCase
import com.maisel.signin.SignInViewState
import com.maisel.state.AuthResultState
import com.maisel.utils.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInComposerController @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase
) {
    /**
     * Creates a [CoroutineScope] that allows us to cancel the ongoing work when the parent
     * ViewModel is disposed.
     */
    private val scope = CoroutineScope(DispatcherProvider.Main)

    /**
     * UI state of the current composer view state.
     */
    private val _stateFlow: MutableStateFlow<SignInViewState> = MutableStateFlow(SignInViewState())

    val state: StateFlow<SignInViewState>
        get() = _stateFlow

    /**
     * UI state of the current composer input.
     */
    val input: MutableStateFlow<AuthenticationState> = MutableStateFlow(AuthenticationState())

    /**ko
     * Represents the validation errors for the current input
     */
    val validationErrors: MutableStateFlow<ValidationError.AuthenticationError> = MutableStateFlow(ValidationError.AuthenticationError())

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current authentication state value.
     */
    fun setSignInInput(value: AuthenticationState) {
        this.input.value = value
    }

    /**
     * Checks the current input for validation errors.
     */
    private fun handleValidationErrors() {
        validationErrors.value = ValidationError.AuthenticationError(
            emailError = !(input.value.email.isNotEmpty() && Validator().isEmailValid(input.value.email))
        )
    }

    /**
     * Makes a login request to sign in the current user
     * @param value Current authentication state value.
     */
    fun makeLoginRequest(value: AuthenticationState) {
        handleValidationErrors()
        if (validationErrors.value.emailError) {
            return
        }
        scope.launch {
            val result = signInUseCase.invoke(value.email, value.password)
            if (result != null && result.user != null) {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Success(result.user!!)) }
            } else {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Error) }
            }
        }
    }

    /**
     * Makes a login request to sign in the authentication provided user
     * @param credential Auth provider credential
     */
    fun signInWithCredential(credential: AuthCredential) {
        scope.launch {
            val result = signInWithCredentialUseCase.invoke(credential)

            if (result != null && result.user != null) {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Success(result.user!!)) }
            } else {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Error) }
            }
        }
    }

    /**
     * Cancels any pending work when the parent ViewModel is about to be destroyed.
     */
    fun onCleared() {
        scope.cancel()
    }
}
