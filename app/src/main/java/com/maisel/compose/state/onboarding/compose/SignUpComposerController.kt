package com.maisel.compose.state.onboarding.compose

import com.maisel.common.state.ValidationError
import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.user.usecase.SignUpUseCase
import com.maisel.signin.SignInViewState
import com.maisel.state.AuthResultState
import com.maisel.utils.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpComposerController @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) {

    private val scope = CoroutineScope(DispatcherProvider.Main)

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
    val validationErrors: MutableStateFlow<ValidationError.AuthenticationError> = MutableStateFlow(
        ValidationError.AuthenticationError()
    )

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current authentication state value.
     */
    fun setSignUpInput(value: AuthenticationState) {
        this.input.value = value
    }

    /**
     * Checks the current input for validation errors.
     */
    private fun handleValidationErrors() {
        validationErrors.value = ValidationError.AuthenticationError(
            nameError = !(input.value.name.isNotEmpty() && Validator().isNameValid(input.value.name)),
            emailError = !(input.value.email.isNotEmpty() && Validator().isEmailValid(input.value.email)),
            passwordError = !(input.value.password.isNotEmpty() && Validator().isNameValid(input.value.password))
        )
    }

    /**
     * Makes a request request to sign up
     * @param value Current authentication state value.
     */
    fun makeSignUpRequest(value: AuthenticationState) {
        handleValidationErrors()
        if (validationErrors.value.nameError || validationErrors.value.emailError || validationErrors.value.passwordError) {
            return
        }
        scope.launch {
            val result = signUpUseCase.invoke(value.name, value.email, value.password)
            if (result != null && result.user != null) {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Success(result.user!!)) }

            } else {
                _stateFlow.update { it.copy(authResultState = AuthResultState.Error) }
            }
        }
    }

}
