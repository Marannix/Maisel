package com.maisel.signup

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
import com.maisel.common.mapper.TextFieldStateMapper
import com.maisel.domain.user.usecase.SignInWithCredentialUseCase
import com.maisel.domain.user.usecase.SignUpUseCase
import com.maisel.navigation.Screens
import com.maisel.utils.ContextProvider
import com.maisel.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val textFieldStateMapper: TextFieldStateMapper,
    private val signUpUseCase: SignUpUseCase,
    private val resourceProvider: ResourceProvider,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val contextProvider: ContextProvider
) : SignUpContract.ViewModel() {

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private val _screenDestinationName = MutableSharedFlow<Screens>()
    val screenDestinationName = _screenDestinationName.asSharedFlow()

    private val _showGoogleSignIn = MutableSharedFlow<Unit>()
    val launchGoogleSignIn = _showGoogleSignIn.asSharedFlow()

    override val _uiState: MutableStateFlow<SignUpContract.UiState> = MutableStateFlow(
        SignUpContract.UiState.initial()
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

    override fun onUiEvent(event: SignUpContract.UiEvents) {
        when (event) {
            is SignUpContract.UiEvents.NameUpdated -> {
                val name = textFieldStateMapper.getNameState(event.name)
                updateUiState { oldState -> oldState.copy(errorMessage = "", name = name) }
            }

            is SignUpContract.UiEvents.EmailUpdated -> {
                val email = textFieldStateMapper.getEmailState(event.email)
                updateUiState { oldState -> oldState.copy(errorMessage = "", email = email) }
            }

            SignUpContract.UiEvents.FacebookButtonClicked -> {
                viewModelScope.launch {
                    _snackbarMessage.emit("Facebook login not implemented")
                }
            }

            SignUpContract.UiEvents.GoogleButtonClicked -> {
                viewModelScope.launch {
                    _showGoogleSignIn.emit(Unit)
                    _snackbarMessage.emit("Google button clicked")
                }
            }

            is SignUpContract.UiEvents.PasswordUpdated -> {
                val password = textFieldStateMapper.getPasswordState(event.password)
                updateUiState { oldState -> oldState.copy(errorMessage = "", password = password) }
            }

            is SignUpContract.UiEvents.SignUpButtonClicked -> {
                viewModelScope.launch {
                    makeSignUpRequest(event.name, event.email, event.password)
                }
            }

            SignUpContract.UiEvents.SignInButtonClicked -> {
                viewModelScope.launch { 
                    updateUiState { oldState -> oldState.copy(navigateToSignIn = true) }
                }
            }
        }
    }

    /**
     * @param value Current authentication state value.
     * Makes a login request to sign up the current user
     * If successful navigate user to Dashboard Screen
     * If error occurs handle error [onSignUpError]
     */
    private fun makeSignUpRequest(name: String, email: String, password: String) {
        viewModelScope.launch {
            updateUiState { oldState -> oldState.copy(isLoading = true, errorMessage = null) }

            try {
                val result = signUpUseCase.invoke(name, email, password)
                if (result != null && result.user != null) {
                    _screenDestinationName.emit(Screens.Dashboard)
                } else {
                    updateUiState { oldState ->
                        oldState.copy(
                            isLoading = false, errorMessage = resourceProvider.getString(
                                R.string.sign_up_error_message
                            )
                        )
                    }
                }
            } catch (throwable: Throwable) {
                onSignUpError(throwable)
            } finally {
                updateUiState { oldState -> oldState.copy(isLoading = false) }
            }
        }
    }

    /**
     * @param throwable Throwable of error that occurred during sign up
     * Handle sign up error
     */
    private fun onSignUpError(throwable: Throwable) {
        viewModelScope.launch {
            _snackbarMessage.emit("onSignUpError: $throwable")
        }
    }
}
