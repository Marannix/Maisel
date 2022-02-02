package com.maisel.onboarding.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.common.composable.CreateEmailAddressTextField
import com.maisel.common.composable.CreatePasswordTextField
import com.maisel.signin.SignInState
import com.maisel.signin.SignInViewModel
import com.maisel.signin.ValidationState
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
    Column(Modifier.fillMaxSize()) {
        val validationError: Boolean =
            viewModel.viewState.observeAsState().value?.signInValidator?.showEmailError ?: false
        val showErrorDialog: Boolean =
            viewModel.viewState.observeAsState().value?.authResultState is AuthResultState.Error
        val emailState = remember { mutableStateOf(TextFieldValue("")) }
        val passwordState = remember { mutableStateOf(TextFieldValue("")) }
        val focusRequester = remember { FocusRequester() }
        val localFocusRequester = LocalFocusManager.current
        localFocusRequester.moveFocus(FocusDirection.Down)

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
    onSignUpClicked: () -> Unit,
    emailContent: @Composable (SignInState) -> Unit = {
        CreateEmailAddressTextField(
            state = it.validationState,
            emailState = signInState.emailInputState,
            modifier = Modifier.focusRequester(signInState.focusRequester) //TODO: Delete probably
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            signInState.localFocusRequester.moveFocus(
                FocusDirection.Down
            )
        }
    },
    passwordContent: @Composable (SignInState) -> Unit = {
        CreatePasswordTextField(
            passwordState = signInState.passwordInputValue,
            showPasswordError = false, //TODO: Actually show error
            modifier = Modifier.focusRequester(signInState.focusRequester) //TODO: Delete probably
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            signInState.localFocusRequester.moveFocus(
                FocusDirection.Down
            )
        }
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

        Text(
            text = "Login to your Account",
            style = MaterialTheme.typography.h3,
            modifier = modifier.padding(bottom = 12.dp)
        )

        ValidationUI(
            viewModel,
            signInState,
            modifier, //TODO: Delete this
            onForgotPasswordClicked,
            emailContent,
            passwordContent
        )

        //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        val signUpText = buildAnnotatedString {
            append("Don't have an account? ")
            withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
                append("Sign up")
            }
        }
        SignInWith(onGoogleClicked, onFacebookClicked)

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = signUpText, style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth()
                    .clickable { onSignUpClicked() }
            )
        }
    }

}

@ExperimentalComposeUiApi
@Composable
private fun ValidationUI(
    viewModel: SignInViewModel,
    signInState: SignInState,
    modifier: Modifier,
    onForgotPasswordClicked: () -> Unit,
    emailContent: @Composable ((SignInState) -> Unit),
    passwordContent: @Composable ((SignInState) -> Unit)
) {
    IncorrectEmailOrPassword(modifier, signInState.showErrorDialog)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    emailContent(signInState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    passwordContent(signInState)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    ForgotPassword(modifier, onForgotPasswordClicked)
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    LoginButton(viewModel, signInState.emailInputState, signInState.passwordInputValue, modifier)
}

@Composable
fun IncorrectEmailOrPassword( //TODO: Rename SignInErrorDialog
    modifier: Modifier,
    showErrorDialog: Boolean
) {
    if (showErrorDialog) { //TODO: Rename SignInError
        Row(
            modifier = modifier
                .padding(4.dp)
                .clip(shapes.medium)
                .background(colorResource(id = R.color.grayBackground)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_lock),
                contentDescription = "Facebook icon",
            )
            Text(
                "Your email address or password is incorrect",
                color = colorResource(id = R.color.red),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
private fun ForgotPassword(modifier: Modifier, onForgotPasswordClicked: () -> Unit) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        Text(
            "Forgot Password?",
            textAlign = TextAlign.End,
            modifier = modifier.clickable { onForgotPasswordClicked() },
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun LoginButton(
    viewModel: SignInViewModel,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    modifier: Modifier
) {
    Button(
        onClick = { viewModel.onLoginClicked(emailState, passwordState) },
        shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp)),
        contentPadding = PaddingValues(16.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 8.dp),
        modifier = modifier.padding(top = 8.dp)
    ) {
        Text(text = "Log In", textAlign = TextAlign.Center)
    }
}


@Composable
private fun SignInWith(onGoogleClicked: () -> Unit, onFacebookClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "- Or sign in with -",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_fb),
                contentDescription = "Facebook icon",
                modifier = Modifier.clickable { onFacebookClicked() }
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_google),
                contentDescription = "Google icon",
                modifier = Modifier.clickable { onGoogleClicked() },
            )
        }
    }
}
