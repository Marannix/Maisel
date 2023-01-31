package com.maisel.signin

import androidx.compose.runtime.Immutable
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel

interface SignInContract {

    abstract class ViewModel : UpdatedBaseViewModel<SignInUiEvents, SignInUiState>()

    @Immutable
    data class SignInUiState(
        val email: String,
        val password: String,
        val error: String,
        val isLoading: Boolean,
    ) : UiStateBase {

        companion object {
            fun initial(email: String = "", password: String = "") = SignInUiState(
                email = email,
                password = password,
                error = "",
                isLoading = false
            )
        }
    }

    sealed class SignInUiEvents : UiEventBase {
        data class EmailUpdated(val email: String) : SignInUiEvents()
        data class PasswordUpdated(val password: String) : SignInUiEvents()
        data class LoginButtonClicked(val email: String, val password: String) : SignInUiEvents()
        object OnForgotPasswordClicked : SignInUiEvents()
        object FacebookButtonClicked : SignInUiEvents()
        object GoogleButtonClicked : SignInUiEvents()
        object SignUpButtonClicked : SignInUiEvents()
    }
}
