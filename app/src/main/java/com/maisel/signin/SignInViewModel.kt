package com.maisel.signin

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ActivityScenario.launch
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.maisel.R
import com.maisel.common.base.BaseViewModel
import com.maisel.common.base.UpdatedBaseViewModel
import com.maisel.common.state.ValidationError
import com.maisel.compose.state.onboarding.compose.AuthenticationFormState
import com.maisel.compose.state.onboarding.compose.SignInComposerController
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import com.maisel.navigation.Screens
import com.maisel.utils.ContextProvider
import com.maisel.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loggedInUser: GetLoggedInUserUseCase,
    private val signInComposerController: SignInComposerController,
    private val resourceProvider: ResourceProvider,
    private val contextProvider: ContextProvider
) : SignInContract.ViewModel() {

    val state: StateFlow<SignInViewState> = signInComposerController.state

    val input: StateFlow<AuthenticationFormState> = signInComposerController.input

    val validationErrors: StateFlow<ValidationError.AuthenticationError> =
        signInComposerController.validationErrors

    private fun signInWithEmailAndPassword(authenticationState: AuthenticationFormState) {
        signInComposerController.makeLoginRequest(authenticationState)
    }

    fun signInWithCredential(credential: AuthCredential) {
        signInComposerController.signInWithCredential(credential)
    }

    fun isUserLoggedIn(): Boolean {
        return loggedInUser.getLoggedInUser() != null
    }

    fun onLoginClicked(authenticationFormState: AuthenticationFormState) {
        signInWithEmailAndPassword(authenticationFormState)
    }

    //TODO: Doesn't work
    fun onLongPressed() {
        signInWithEmailAndPassword(AuthenticationFormState("laptop@admin.com", "Password2"))
    }

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
    fun setSignInInput(value: AuthenticationFormState): Unit =
        signInComposerController.setSignInInput(value)

    fun setIdleState() {
        signInComposerController.setIdleState()
    }

    fun onGoogleSignInActivityResult(data: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = data.getResult(ApiException::class.java)
            Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
            signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w("TAG", "Google sign in failed", e)
        }
    }

    fun getGoogleLoginAuth(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resourceProvider.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(contextProvider.getContext(), gso)
    }

    /**
     * UI Events to update [SignInScreen].
     */
    override fun onUiEvent(event: SignInContract.UiEvents) {
        when (event) {
            is SignInContract.UiEvents.LoginButtonClicked -> {
                viewModelScope.launch {

                }
            }
            SignInContract.UiEvents.FacebookButtonClicked -> {
                viewModelScope.launch {

                }
            }
            SignInContract.UiEvents.GoogleButtonClicked -> {
                viewModelScope.launch {

                }
            }
            SignInContract.UiEvents.SignUpButtonClicked -> {
                viewModelScope.launch {
                  //  navHostController.navigate(Screens.SignUp.name)
                }
            }
            SignInContract.UiEvents.OnForgotPasswordClicked -> {

            }
        }
    }

    /**
     * Disposes the inner [SignInComposerController].
     */
    override fun onCleared() {
        super.onCleared()
        signInComposerController.onCleared()
    }
}
