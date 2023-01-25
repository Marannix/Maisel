package com.maisel.signin

import com.maisel.common.base.BaseViewModel
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel

interface SignInContract {

    abstract class ViewModel : UpdatedBaseViewModel<UiEvents, SignInUiState>()

    object SignInUiState : UiStateBase

    sealed class UiEvents : UiEventBase {
        data class LoginButtonClicked(val email: String, val password: String) : UiEvents()
        object OnForgotPasswordClicked : UiEvents()
        object FacebookButtonClicked : UiEvents()
        object GoogleButtonClicked : UiEvents()
        object SignUpButtonClicked : UiEvents()
    }
}
