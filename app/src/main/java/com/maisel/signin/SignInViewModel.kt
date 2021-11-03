package com.maisel.signin

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.maisel.common.BaseViewModel
import com.maisel.domain.user.usecase.GetCurrentUser
import com.maisel.domain.user.usecase.SetCurrentUserUseCase
import com.maisel.domain.user.usecase.SignInUseCase
import com.maisel.domain.user.usecase.SignInWithCredentialUseCase
import com.maisel.state.AuthResultState
import com.maisel.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val signInUseCase: SignInUseCase,
                                          private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
                                          private val currentUser: GetCurrentUser,
                                          private val setCurrentUser: SetCurrentUserUseCase
) : BaseViewModel() {

    val viewState = MutableLiveData<SignInViewState>()

    init {
        viewState.value = SignInViewState()
    }

    private fun currentViewState(): SignInViewState = viewState.value!!

    fun signInWithEmailAndPassword(email: String, password: String) {
        signInUseCase.invoke(email, password)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.value = currentViewState().copy(authResultState = AuthResultState.Loading  )}
            .subscribe ({
                if (it.user != null) {
                    viewState.value = currentViewState().copy(authResultState = AuthResultState.Success(it.user!!))
                } else {
                    //TODO: Throw specific error for null user
                    viewState.value = currentViewState().copy(authResultState = AuthResultState.Error)
                }
            }, {
                viewState.value = currentViewState().copy(authResultState = AuthResultState.Error)
            })
            .addDisposable()
    }

    fun signInWithCredential(idToken: String, credential: AuthCredential) {
        signInWithCredentialUseCase.invoke(idToken, credential).observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.value = currentViewState().copy(authResultState = AuthResultState.Loading  )}
            .subscribe ({
                if (it.user != null) {
                    viewState.value = currentViewState().copy(authResultState = AuthResultState.Success(it.user!!))
                } else {
                    //TODO: Throw specific error for null user
                    viewState.value = currentViewState().copy(authResultState = AuthResultState.Error)
                }
            }, {
                viewState.value = currentViewState().copy(authResultState = AuthResultState.Error)
            })
            .addDisposable()
    }

    fun isEmailAddressValid(email: String) : Boolean {
        return if (email.isNotEmpty() && Validator().isEmailValid(email)) {
            viewState.value = currentViewState().copy(signInValidator = currentViewState().signInValidator.copy(showEmailError = false))
            true
        } else {
            viewState.value = currentViewState().copy(signInValidator = currentViewState().signInValidator.copy(showEmailError = true))
            false
        }
    }

    fun isUserLoggedIn(): Boolean {
        return currentUser.invoke() != null
    }

    fun setUser(user: FirebaseUser) {
        setCurrentUser.invoke(user)
    }

    fun onLoginClicked(emailState: MutableState<TextFieldValue>, passwordState: MutableState<TextFieldValue>) {
        if (isEmailAddressValid(emailState.value.text)) {
            signInWithEmailAndPassword(emailState.value.text, password = passwordState.value.text)
        }
    }
}