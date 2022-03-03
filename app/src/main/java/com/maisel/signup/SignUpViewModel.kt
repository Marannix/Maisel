package com.maisel.signup

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.compose.state.onboarding.compose.SignUpForm
import com.maisel.domain.user.usecase.SignUpUseCase
import com.maisel.state.AuthResultState
import com.maisel.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) :
    BaseViewModel() {

    val viewState = MutableLiveData<SignUpViewState>()

    init {
        viewState.value = SignUpViewState()
    }

    private fun currentViewState(): SignUpViewState = viewState.value!!

    private fun registerUser(name: String, email: String, password: String) {
        signUpUseCase.invoke(name, email, password)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.value = currentViewState().copy(authResultState = AuthResultState.Loading)
            }
            .subscribe({
                if (it.user != null) {
                    viewState.value =
                        currentViewState().copy(authResultState = AuthResultState.Success(it.user!!))
                } else {
                    //TODO: Throw specific error for null user
                    viewState.value =
                        currentViewState().copy(authResultState = AuthResultState.Error)
                }
            }, {
                viewState.value = currentViewState().copy(authResultState = AuthResultState.Error)
            })
            .addDisposable()
    }

    private fun isEmailAddressValid(email: String): Boolean {
        return if (email.isNotEmpty() && Validator().isEmailValid(email)) {
            viewState.value = currentViewState().copy(
                signUpValidator = currentViewState().signUpValidator.copy(showEmailError = false)
            )
            true
        } else {
            viewState.value = currentViewState().copy(
                signUpValidator = currentViewState().signUpValidator.copy(showEmailError = true)
            )
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return if (password.isNotEmpty() && Validator().isPasswordValid(password)) {
            viewState.value = currentViewState().copy(
                signUpValidator = currentViewState().signUpValidator.copy(showPasswordError = false)
            )
            true
        } else {
            viewState.value = currentViewState().copy(
                signUpValidator = currentViewState().signUpValidator.copy(showPasswordError = true)
            )
            false
        }
    }

    private fun isNameValid(name: String): Boolean {
        return if (name.isNotEmpty()) {
            viewState.value = currentViewState().copy(
                signUpValidator = currentViewState().signUpValidator.copy(showNameError = false)
            )
            true
        } else {
            viewState.value = currentViewState().copy(
                signUpValidator = currentViewState().signUpValidator.copy(showNameError = true)
            )
            false
        }
    }

    fun onSignUpClicked(signUpForm: SignUpForm) {
        if (isNameValid(signUpForm.name) && isEmailAddressValid(signUpForm.email) && isPasswordValid(signUpForm.password)) {
            registerUser(signUpForm.name, signUpForm.email, signUpForm.password)
        }
    }
}
