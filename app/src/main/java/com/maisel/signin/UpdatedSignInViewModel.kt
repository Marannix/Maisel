package com.maisel.signin

import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.maisel.R
import com.maisel.compose.state.onboarding.compose.AuthenticationFormState
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import com.maisel.domain.user.usecase.SignInUseCase
import com.maisel.domain.user.usecase.SignInWithCredentialUseCase
import com.maisel.navigation.Screens
import com.maisel.utils.ContextProvider
import com.maisel.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatedSignInViewModel @Inject constructor(
    private val loggedInUser: GetLoggedInUserUseCase,
    private val signInUseCase: SignInUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val resourceProvider: ResourceProvider,
    private val contextProvider: ContextProvider
) : SignInContract.ViewModel() {

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private val _screenDestinationName = MutableSharedFlow<Screens>()
    val screenDestinationName = _screenDestinationName.asSharedFlow()

    private val _showGoogleSignIn = MutableSharedFlow<Unit>()
    val launchGoogleSignIn = _showGoogleSignIn.asSharedFlow()

//    val state: StateFlow<SignInViewState> = signInComposerController.state
//
//    val input: StateFlow<AuthenticationFormState> = signInComposerController.input
//
//    val validationErrors: StateFlow<ValidationError.AuthenticationError> =
//        signInComposerController.validationErrors

//    private fun signInWithEmailAndPassword(authenticationState: AuthenticationFormState) {
//        //      signInComposerController.makeLoginRequest(authenticationState)
//    }

    private fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            signInWithCredentialUseCase.invoke(credential)
        }
    }

//    fun isUserLoggedIn(): Boolean {
//        return loggedInUser.getLoggedInUser() != null
//    }

//    fun onLoginClicked(authenticationFormState: AuthenticationFormState) {
//        signInWithEmailAndPassword(authenticationFormState)
//    }

    //TODO: Doesn't work
//    fun onLongPressed() {
//        signInWithEmailAndPassword(AuthenticationFormState("laptop@admin.com", "Password2"))
//    }

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
    fun setSignInInput(value: AuthenticationFormState): Unit {
        //     signInComposerController.setSignInInput(value)
    }

    fun setIdleState() {
        ///    signInComposerController.setIdleState()
    }

    fun onGoogleSignInActivityResult(data: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = data.getResult(ApiException::class.java)
            signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))
        } catch (exception: ApiException) {
            // Google Sign In failed, update UI appropriately
            viewModelScope.launch {
                _snackbarMessage.emit("Google sign in failed: $exception")
            }
        }
    }

    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resourceProvider.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(contextProvider.getContext(), gso)
    }

    /**
     * UI Events to update [UpdatedSignInScreen].
     */
    override fun onUiEvent(event: SignInContract.SignInUiEvents) {
        when (event) {
            is SignInContract.SignInUiEvents.EmailUpdated -> {
                updateUiState { oldState -> oldState.copy(error = "", email = event.email) }
            }
            is SignInContract.SignInUiEvents.PasswordUpdated -> {
                updateUiState { oldState -> oldState.copy(error = "", password = event.password) }
            }
            is SignInContract.SignInUiEvents.LoginButtonClicked -> {
                viewModelScope.launch {
                    //    makeLoginRequest()
                    _snackbarMessage.emit("Login clicked")
                }
            }
            SignInContract.SignInUiEvents.FacebookButtonClicked -> {
                viewModelScope.launch {
                    _snackbarMessage.emit("Facebook login not implemented")
                }
            }
            SignInContract.SignInUiEvents.GoogleButtonClicked -> {
                viewModelScope.launch {
                    _showGoogleSignIn.emit(Unit)
                    _snackbarMessage.emit("Google button clicked")
                }
            }
            SignInContract.SignInUiEvents.SignUpButtonClicked -> {
                viewModelScope.launch {
                    _screenDestinationName.emit(Screens.SignUp)
                }
            }
            SignInContract.SignInUiEvents.OnForgotPasswordClicked -> {
                viewModelScope.launch {
                    _snackbarMessage.emit("Forgot password not implemented")
                }
            }
        }
    }

    /**
     * @param value Current authentication state value.
     * Makes a login request to sign in the current user
     * If successful navigate user to Dashboard Screen
     * If error occurs handle error [onSignInError]
     */
    fun makeLoginRequest(value: AuthenticationFormState) {
        viewModelScope.launch {
            try {
                signInUseCase.invoke(value.email, value.password)
                _screenDestinationName.emit(Screens.Dashboard)
            } catch (throwable: Throwable) {
                onSignInError(throwable)
            } finally {

            }
        }
    }

    /**
     * @param throwable Throwable of error that occurred during sign in
     * Handle sign in error
     */
    private fun onSignInError(throwable: Throwable) {
        viewModelScope.launch {
            _snackbarMessage.emit("onSignInError: $throwable")
        }
    }

//        handleValidationErrors()
//        if (validationErrors.value.emailError) {
//            return
//        }
//        viewModelScope.launch {
//            val result = signInUseCase.invoke(value.email, value.password)
//            if (result != null && result.user != null) {
//                _stateFlow.update { it.copy(authResultState = AuthResultState.Success(result.user!!)) }
//
//            } else {
//                _stateFlow.update { it.copy(authResultState = AuthResultState.Error) }
//            }
//        }


}
