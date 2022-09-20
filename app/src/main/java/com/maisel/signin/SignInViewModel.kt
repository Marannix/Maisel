package com.maisel.signin

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.maisel.common.BaseViewModel
import com.maisel.common.state.ValidationError
import com.maisel.compose.state.onboarding.compose.AuthenticationState
import com.maisel.compose.state.onboarding.compose.SignInComposerController
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loggedInUser: GetLoggedInUserUseCase,
    private val signInComposerController: SignInComposerController
) : BaseViewModel() {

    val state: StateFlow<SignInViewState> = signInComposerController.state

    val input: StateFlow<AuthenticationState> = signInComposerController.input

    val validationErrors: StateFlow<ValidationError.AuthenticationError> = signInComposerController.validationErrors

    private fun signInWithEmailAndPassword(authenticationState: AuthenticationState) {
        signInComposerController.makeLoginRequest(authenticationState)
    }

    fun signInWithCredential(credential: AuthCredential) {
        signInComposerController.signInWithCredential(credential)
    }

    fun isUserLoggedIn(): Boolean {
        return loggedInUser.getLoggedInUser() != null
    }

    fun onLoginClicked(authenticationState: AuthenticationState) {
        signInWithEmailAndPassword(authenticationState)
    }

    //TODO: Doesn't work
    fun onLongPressed() {
        signInWithEmailAndPassword(AuthenticationState("laptop@admin.com", "Password2"))
    }

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
     fun setSignInInput(value: AuthenticationState): Unit = signInComposerController.setSignInInput(value)

    fun stuff(data: Task<GoogleSignInAccount>) {
      //  val task = GoogleSignIn.getSignedInAccountFromIntent(data)
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
    /**
     * Disposes the inner [SignInComposerController].
     */
    override fun onCleared() {
        super.onCleared()
        signInComposerController.onCleared()
    }
}
