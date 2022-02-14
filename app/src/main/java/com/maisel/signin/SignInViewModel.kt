package com.maisel.signin

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.maisel.common.BaseViewModel
import com.maisel.common.state.ValidationError
import com.maisel.compose.state.onboarding.compose.AuthenticationState
import com.maisel.compose.state.onboarding.compose.SignInComposerController
import com.maisel.domain.user.usecase.GetCurrentUser
import com.maisel.domain.user.usecase.SetCurrentUserUseCase
import com.maisel.domain.user.usecase.SignInWithCredentialUseCase
import com.maisel.state.AuthResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val currentUser: GetCurrentUser,
    private val setCurrentUser: SetCurrentUserUseCase,
    private val signInComposerController: SignInComposerController
) : BaseViewModel() {

    val viewState = MutableLiveData<SignInViewState>()

    val state: StateFlow<SignInViewState> = signInComposerController.state

    val input: StateFlow<AuthenticationState> = signInComposerController.input

    val validationErrors: StateFlow<ValidationError.AuthenticationError> = signInComposerController.validationErrors

    init {
        viewState.value = SignInViewState()
    }

    private fun currentViewState(): SignInViewState = viewState.value!!

    private fun signInWithEmailAndPassword(authenticationState: AuthenticationState) {
        signInComposerController.makeLoginRequest(authenticationState)
    }

    fun signInWithCredential(idToken: String, credential: AuthCredential) {
        signInWithCredentialUseCase.invoke(idToken, credential)
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

    fun isUserLoggedIn(): Boolean {
        return currentUser.invoke() != null
    }

    fun setUser(user: FirebaseUser) {
        setCurrentUser.invoke(user)
    }

    fun onLoginClicked(authenticationState: AuthenticationState) {
        signInWithEmailAndPassword(authenticationState)
    }

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
     fun setSignInInput(value: AuthenticationState): Unit = signInComposerController.setSignInInput(value)

    /**
     * Disposes the inner [SignInComposerController].
     */
    override fun onCleared() {
        super.onCleared()
        signInComposerController.onCleared()
    }
}
