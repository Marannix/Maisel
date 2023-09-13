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
import com.maisel.common.composable.TextFieldState
import com.maisel.domain.user.usecase.SignInUseCase
import com.maisel.domain.user.usecase.SignInWithCredentialUseCase
import com.maisel.navigation.Screens
import com.maisel.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val textFieldValidator: TextFieldValidator,
    private val resourceProvider: ResourceProvider,
    private val contextProvider: ContextProvider
) : SignInContract.ViewModel() {

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private val _screenDestinationName = MutableSharedFlow<Screens>()
    val screenDestinationName = _screenDestinationName.asSharedFlow()

    private val _showGoogleSignIn = MutableSharedFlow<Unit>()
    val launchGoogleSignIn = _showGoogleSignIn.asSharedFlow()

    override val _uiState: MutableStateFlow<SignInContract.SignInUiState> = MutableStateFlow(
        SignInContract.SignInUiState.initial()
    )

    private fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            signInWithCredentialUseCase.invoke(credential)
        }
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
                val email = getEmailState(event.email)
                updateUiState { oldState -> oldState.copy(errorMessage = "", email = email) }
            }
            is SignInContract.SignInUiEvents.PasswordUpdated -> {
                val password = getPasswordState(event.password)
                updateUiState { oldState -> oldState.copy(errorMessage = "", password = password) }
            }
            is SignInContract.SignInUiEvents.LoginButtonClicked -> {
                viewModelScope.launch {
                    makeLoginRequest(event.email, event.password)
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
    private fun makeLoginRequest(email: String, password: String) {
        viewModelScope.launch {
            updateUiState { oldState -> oldState.copy(isLoading = true, errorMessage = null) }

            try {
                val result = signInUseCase.invoke(email, password)
                if (result.isSuccess) {
                    _screenDestinationName.emit(Screens.Dashboard)
                } else {
                    updateUiState { oldState -> oldState.copy(isLoading = false, errorMessage = resourceProvider.getString(R.string.sign_in_error_message)) }
                }
            } catch (throwable: Throwable) {
                onSignInError(throwable)
            } finally {
                updateUiState { oldState -> oldState.copy(isLoading = false) }
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

    private fun getEmailState(email: String): TextFieldState {
        val emailState = when (textFieldValidator.validateEmail(email)) {
            FieldValidationResult.EMPTY -> TextFieldState.Invalid(
                email,
                "Yikes! No email address provided."
            )
            FieldValidationResult.INVALID -> TextFieldState.Invalid(
                email,
                "Oops! Please enter a valid email address."
            )
            FieldValidationResult.VALID -> TextFieldState.Valid(email)
        }

        return emailState
    }

    private fun getPasswordState(password: String): TextFieldState {
        val passwordState = when (textFieldValidator.validatePassword(password)) {
            FieldValidationResult.EMPTY -> TextFieldState.Invalid(
                password,
                "Oops! I think you forgot to enter a password"
            )
            FieldValidationResult.INVALID -> TextFieldState.Invalid(
                password,
                "Oops! Please enter a valid password"
            )
            FieldValidationResult.VALID -> TextFieldState.Valid(password)
        }

        return passwordState
    }
}
