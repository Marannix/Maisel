package com.maisel.onboarding.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.common.composable.DefaultEmailAddressContent
import com.maisel.common.composable.DefaultPasswordContent
import com.maisel.compose.state.onboarding.compose.SignInForm
import com.maisel.compose.state.onboarding.compose.SignInState
import com.maisel.compose.state.onboarding.compose.ValidationState
import com.maisel.compose.ui.components.DefaultCallToActionButton
import com.maisel.compose.ui.components.OnboardingUserHeader
import com.maisel.compose.ui.components.onboarding.OnboardingAlternativeLoginFooter
import com.maisel.compose.ui.components.onboarding.ForgotPassword
import com.maisel.compose.ui.components.onboarding.OnboardingUserFooter
import com.maisel.signin.SignInViewModel
import com.maisel.state.AuthResultState
import com.maisel.ui.shapes

@Composable
@ExperimentalComposeUiApi
@Preview(device = PIXEL_4)
fun SignInPage(
    viewModel: SignInViewModel,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
) {
    val validationError: Boolean =
        viewModel.viewState.observeAsState().value?.signInValidator?.showEmailError ?: false
    val showErrorDialog: Boolean =
        viewModel.viewState.observeAsState().value?.authResultState is AuthResultState.Error
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val localFocusRequester = LocalFocusManager.current

    Column(Modifier.fillMaxSize()) {
        SignUpMainCard(
            viewModel = viewModel,
            signInState = SignInState(
                ValidationState(
                    showEmailError = validationError,
                    showPasswordError = false
                ),
                showErrorDialog,
                emailState,
                passwordState,
                signInForm = SignInForm(
                    emailState.value.text,
                    passwordState.value.text
                ),
                focusRequester,
                localFocusRequester
            ),
            onGoogleClicked = onGoogleClicked,
            onFacebookClicked = onFacebookClicked,
            onForgotPasswordClicked = onForgotPasswordClicked,
            onSignUpClicked = onSignUpClicked
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun SignUpMainCard(
    viewModel: SignInViewModel,
    signInState: SignInState,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignIn: () -> Unit = { viewModel.onLoginClicked(signInState.signInForm) },
    onSignUpClicked: () -> Unit,
    emailContent: @Composable (SignInState) -> Unit = {
        DefaultEmailAddressContent(
            state = it.validationState,
            emailState = it.emailInputState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    },
    passwordContent: @Composable (SignInState) -> Unit = {
        DefaultPasswordContent(
            state = it.validationState,
            passwordState = it.passwordInputValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    }, errorBanner: @Composable (SignInState) -> Unit = {
        SignInErrorBanner(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            it.showErrorBanner
        )
    }
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
            ImageVector.vectorResource(id = R.drawable.ic_son_goku),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp),
        )

        OnboardingUserHeader("Login to your Account", modifier.padding(bottom = 12.dp))

        ValidationUI(
            signInState,
            modifier, //TODO: Delete this
            onForgotPasswordClicked,
            emailContent,
            passwordContent,
            errorBanner,
            onSignIn
        )

        //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        OnboardingAlternativeLoginFooter(onGoogleClicked, onFacebookClicked, "- Or sign in with -")

        OnboardingUserFooter("Don't have an account? ", "Sign up", onSignUpClicked)
    }
}

@ExperimentalComposeUiApi
@Composable
private fun ValidationUI(
    signInState: SignInState,
    modifier: Modifier,
    onForgotPasswordClicked: () -> Unit,
    emailContent: @Composable (SignInState) -> Unit,
    passwordContent: @Composable (SignInState) -> Unit,
    errorBanner: @Composable (SignInState) -> Unit,
    onSignIn: () -> Unit
) {
    errorBanner(signInState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    emailContent(signInState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    passwordContent(signInState)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    ForgotPassword("Forgot Password?", onForgotPasswordClicked, modifier)
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    DefaultCallToActionButton(onSignIn, "Sign in")
}

@Composable
fun SignInErrorBanner(
    modifier: Modifier,
    showErrorBanner: Boolean
) {
    if (showErrorBanner) {
        Box(
            modifier = modifier
                .clip(shapes.small)
                .background(colorResource(id = R.color.maisel_compose_error_accent))
        ) {
            Row(
                modifier = modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
                    contentDescription = "Facebook icon",
                )
                Text(
                    text = "Your email address or password is incorrect",
                    color = colorResource(id = R.color.white),
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }
}
