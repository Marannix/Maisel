package com.maisel.signin

import androidx.lifecycle.ViewModel
import com.maisel.compose.state.onboarding.compose.SignInComposerController
import com.maisel.compose.state.onboarding.compose.SignInForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

//@HiltViewModel
//class SignInComposerViewModel(
//    private val signInComposerController: SignInComposerController
//) : ViewModel() {
//
//    /**
//     * UI state of the current composer input.
//     */
//    val input: MutableStateFlow<SignInForm> = signInComposerController.input
//
//    /**
//     * Called when the input changes and the internal state needs to be updated.
//     *
//     * @param value Current state value.
//     */
//    fun setSignInForm(value: SignInForm): Unit = signInComposerController.setSignInInput(value)
//
//
//    /**
//     * Signs in
//     *
//     * @param message The message to send.
//     */
//    suspend fun signIn(form: SignInForm): Unit = signInComposerController.signIn(form)
//}
