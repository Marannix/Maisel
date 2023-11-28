package com.maisel.signup

import androidx.compose.runtime.Immutable
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel
import com.maisel.common.composable.TextFieldState

interface SignUpContract {

    abstract class ViewModel : UpdatedBaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val name: TextFieldState,
        val email: TextFieldState,
        val password: TextFieldState,
        val errorMessage: String?,
        val isLoading: Boolean,
        val navigateToSignIn: Boolean,
    ) : UiStateBase {

        companion object {
            fun initial(name: String = "", email: String = "", password: String = "") =
                UiState(
                    name = if (name.isNotEmpty()) TextFieldState.Valid(name) else TextFieldState.Empty,
                    email = if (email.isNotEmpty()) TextFieldState.Valid(email) else TextFieldState.Empty,
                    password = if (password.isNotEmpty()) TextFieldState.Valid(password) else TextFieldState.Empty,
                    errorMessage = null,
                    isLoading = false,
                    navigateToSignIn = false,
                )
        }
    }

    sealed class UiEvents : UiEventBase {
        data class NameUpdated(val name: String) : UiEvents()
        data class EmailUpdated(val email: String) : UiEvents()
        data class PasswordUpdated(val password: String) : UiEvents()
        object FacebookButtonClicked : UiEvents()
        object SignInButtonClicked : UiEvents()
        object GoogleButtonClicked : UiEvents()
        data class SignUpButtonClicked(val name: String, val email: String, val password: String) :
            UiEvents()
    }
}
