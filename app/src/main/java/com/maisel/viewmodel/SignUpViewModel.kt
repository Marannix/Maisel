package com.maisel.viewmodel

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.domain.user.usecase.SignUpUseCase
import com.maisel.state.AuthResultState
import com.maisel.state.SignUpViewState
import com.maisel.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) : BaseViewModel() {
    val viewState = MutableLiveData<SignUpViewState>()

    init {
        viewState.value = SignUpViewState()
    }

    private fun currentViewState(): SignUpViewState = viewState.value!!

    fun registerUser(email: String, address: String, password: String) {
            signUpUseCase.invoke(email, address, password)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.value = currentViewState().copy(authResultState = AuthResultState.Loading  )}
                .subscribe ({
                    viewState.value = currentViewState().copy(authResultState = AuthResultState.Success)
                }, {
                    viewState.value = currentViewState().copy(authResultState = AuthResultState.Error)
                })
                .addDisposable()
    }

    fun isEmailAddressValid(email: String) : Boolean {
        return if (email.isNotEmpty() && Validator().isEmailValid(email)) {
            viewState.value = currentViewState().copy(signUpValidator = currentViewState().signUpValidator.copy(showEmailError = false))
            true
        } else {
            viewState.value = currentViewState().copy(signUpValidator = currentViewState().signUpValidator.copy(showEmailError = true))
            false
        }
    }

    fun isPasswordValid(password: String) : Boolean {
        return if (password.isNotEmpty() && Validator().isPasswordValid(password)) {
            viewState.value = currentViewState().copy(signUpValidator = currentViewState().signUpValidator.copy(showPasswordError = false))
            true
        } else {
            viewState.value = currentViewState().copy(signUpValidator = currentViewState().signUpValidator.copy(showPasswordError = true))
            false
        }
    }

    fun isNameValid(name: String) : Boolean {
        return if (name.isNotEmpty()) {
            viewState.value = currentViewState().copy(signUpValidator = currentViewState().signUpValidator.copy(showNameError = false))
            true
        } else {
            viewState.value = currentViewState().copy(signUpValidator = currentViewState().signUpValidator.copy(showNameError = true))
            false
        }
    }
}