package com.maisel.onboarding.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.common.composable.CreatePasswordTextField
import com.maisel.signin.SignInViewModel

@Composable
@Preview(device = PIXEL_4)
fun SignUpPage(viewModel: SignInViewModel, showEmailError: Boolean = false) {
    Column(Modifier.fillMaxSize()) {
        SignUpMainCard(viewModel, showEmailError)
    }
}

@Composable
fun SignUpMainCard(viewModel: SignInViewModel, showEmailError: Boolean) {
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
            focusRequester
        )

        //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        val signUpText = buildAnnotatedString {
            append("Don't have an account? ")
            withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
                append("Sign up")
            }
        }
        SignInWith()

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
            )
        }
    }

}

@Composable
private fun ValidationUI(
    viewModel: SignInViewModel,
    emailState: MutableState<TextFieldValue>,
    showEmailError: Boolean,
    passwordState: MutableState<TextFieldValue>,
    modifier: Modifier,
    focusRequester: FocusRequester
) {
    CreateEmailAddressTextField(emailState, showEmailError, modifier, focusRequester)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    CreatePasswordTextField(passwordState, modifier)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    ForgotPassword(modifier)
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    LoginButton(viewModel, emailState, passwordState, modifier)
}


@Composable
fun CreateEmailAddressTextField(
    emailState: MutableState<TextFieldValue>,
    showEmailError: Boolean,
    modifier: Modifier,
    focusRequester: FocusRequester
) {

    OutlinedTextField(
        modifier = modifier,
        value = emailState.value, onValueChange = {
            emailState.value = it
        },
        label = {
            Text(text = "Email")
        },
        placeholder = {
            Text(text = "Email")
        },
        isError = showEmailError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        keyboardActions = KeyboardActions(
            onNext = { focusRequester.requestFocus() }
        )
    )
    if (showEmailError) {
        Text(
            text = "Please enter a valid email",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ForgotPassword(modifier: Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        Text(
            "Forgot Password?",
            textAlign = TextAlign.End,
            modifier = modifier,
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
private fun SignInWith() {
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
                contentDescription = "Facebook icon"
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_google),
                contentDescription = "Google icon"
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
    }
}