package com.maisel.viewmodel

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.state.AuthResultState
import com.maisel.state.SignUpViewState
import com.maisel.usecase.SignUpUseCase
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
}