package com.maisel.showcase.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.compose.state.onboarding.compose.ValidationState
import com.maisel.compose.state.onboarding.compose.SignUpForm
import com.maisel.compose.state.onboarding.compose.SignUpState
import com.maisel.compose.ui.components.DefaultCallToActionButton
import com.maisel.compose.ui.components.OnboardingUserHeader
import com.maisel.compose.ui.components.onboarding.OnboardingAlternativeLoginFooter
import com.maisel.compose.ui.components.onboarding.OnboardingUserFooter
import com.maisel.signup.SignUpViewModel

@ExperimentalComposeUiApi
@Composable
@Preview(device = Devices.PIXEL_4)
fun SignUpPage(
    viewModel: SignUpViewModel,
    onGoogleClicked: () -> Unit = { },
    onFacebookClicked: () -> Unit = { }
) {
    val showNameError =
        viewModel.viewState.observeAsState().value?.signUpValidator?.showNameError ?: false
    val showEmailError =
        viewModel.viewState.observeAsState().value?.signUpValidator?.showEmailError ?: false
    val showPasswordError =
        viewModel.viewState.observeAsState().value?.signUpValidator?.showPasswordError ?: false
    val nameState = remember { mutableStateOf(TextFieldValue("")) }
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val localFocusRequester = LocalFocusManager.current

    Column(Modifier.fillMaxSize()) {
        SignUpMainCard(
            viewModel = viewModel,
            signUpState = SignUpState(
                ValidationState(
                    showNameError = showNameError,
                    showEmailError = showEmailError,
                    showPasswordError = showPasswordError
                ),
                nameInputState = nameState,
                emailInputState = emailState,
                passwordInputValue = passwordState,
                signUpForm = SignUpForm(
                    nameState.value.text,
                    emailState.value.text,
                    passwordState.value.text
                ),
                focusRequester,
                localFocusRequester,
            ),
            onGoogleClicked = onGoogleClicked,
            onFacebookClicked = onFacebookClicked
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun SignUpMainCard(
    viewModel: SignUpViewModel,
    signUpState: SignUpState,
    onSignUp: () -> Unit = { viewModel.onSignUpClicked(signUpState.signUpForm) },
    onSignIn: () -> Unit = { },
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
//    nameContent: @Composable (SignUpState) -> Unit = {
//        DefaultNameContent(
//            state = it.validationState,
//            nameState = it.nameInputState,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        ) {
//                 it.localFocusRequester.moveFocus(FocusDirection.Down)
//        }
//    },
//    emailContent: @Composable (SignUpState) -> Unit = {
//        DefaultEmailAddressContent(
//            state = it.validationState,
//            emailState = it.emailInputState,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        ) {
//                it.localFocusRequester.moveFocus(FocusDirection.Down)
//        }
//    },
//    passwordContent: @Composable (SignUpState) -> Unit = {
//        DefaultPasswordContent(
//            state = it.validationState,
//            passwordState = it.passwordInputValue,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        ) {
//            it.localFocusRequester.moveFocus(FocusDirection.Down)
//        }
//    }
) {

    val scrollState = rememberScrollState()

    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            ImageVector.vectorResource(id = R.drawable.ic_stich),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp),
        )

        OnboardingUserHeader("Create your Account", modifier.padding(bottom = 12.dp))

//        SignUpValidationUI(
//            signUpState,
//            nameContent,
//            emailContent,
//            passwordContent,
//            onSignUp
//        )

        //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        OnboardingAlternativeLoginFooter(onGoogleClicked, onFacebookClicked, "- Or sign up with -")

        OnboardingUserFooter("Already have an account? ", "Sign in", onSignIn)
    }
}

@ExperimentalComposeUiApi
@Composable
private fun SignUpValidationUI(
    signUpState: SignUpState,
    nameContent: @Composable (SignUpState) -> Unit,
    emailContent: @Composable (SignUpState) -> Unit,
    passwordContent: @Composable (SignUpState) -> Unit,
    onSignUp: () -> Unit
) {
    nameContent(signUpState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    emailContent(signUpState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    passwordContent(signUpState)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    DefaultCallToActionButton(onSignUp, "Sign up")
}
