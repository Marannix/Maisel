package com.maisel.onboarding.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
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
import com.maisel.signin.SignInViewModel
import com.maisel.ui.shapes

@Composable
@ExperimentalComposeUiApi
@Preview(device = PIXEL_4)
fun SignInPage(
    viewModel: SignInViewModel,
    showEmailError: Boolean = false,
    showErrorDialog: Boolean,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        //TODO: Can I save these into a variable?
        SignUpMainCard(
            viewModel,
            showEmailError,
            showErrorDialog,
            onGoogleClicked,
            onFacebookClicked,
            onForgotPasswordClicked,
            onSignUpClicked
        )
    }
}

@Composable
fun SignUpMainCard(
    viewModel: SignInViewModel,
    showEmailError: Boolean,
    showErrorDialog: Boolean,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

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
            emailState,
            showEmailError,
            passwordState,
            modifier.focusRequester(focusRequester),
            focusRequester,
            onForgotPasswordClicked,
            showErrorDialog
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
                    .fillMaxWidth().clickable { onSignUpClicked() }
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ValidationUI(
    viewModel: SignInViewModel,
    emailState: MutableState<TextFieldValue>,
    showEmailError: Boolean,
    passwordState: MutableState<TextFieldValue>,
    modifier: Modifier,
    focusRequester: FocusRequester,
    onForgotPasswordClicked: () -> Unit,
    showErrorDialog: Boolean
) {
    IncorrectEmailOrPassword(modifier, showErrorDialog)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    CreateEmailAddressTextField(emailState, showEmailError, modifier, focusRequester)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    CreatePasswordTextField(passwordState, false, modifier, focusRequester)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    ForgotPassword(modifier, onForgotPasswordClicked)
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    LoginButton(viewModel, emailState, passwordState, modifier)
}

@Composable
fun IncorrectEmailOrPassword(
    modifier: Modifier,
    showErrorDialog: Boolean
) {
    if (showErrorDialog) {
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
