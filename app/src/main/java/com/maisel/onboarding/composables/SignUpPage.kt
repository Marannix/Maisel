package com.maisel.onboarding.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.common.composable.CreateEmailAddressTextField
import com.maisel.common.composable.CreateNameTextField
import com.maisel.common.composable.CreatePasswordTextField
import com.maisel.signin.ValidationState
import com.maisel.signup.SignUpForm
import com.maisel.signup.SignUpState
import com.maisel.signup.SignUpViewModel

@ExperimentalComposeUiApi
@Composable
@Preview(device = Devices.PIXEL_4)
fun SignUpPage(
    viewModel: SignUpViewModel
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
    //localFocusRequester.moveFocus(FocusDirection.Down)

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
                localFocusRequester
            )
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun SignUpMainCard(
    viewModel: SignUpViewModel,
    signUpState: SignUpState,
    onSignUp: (SignUpState) -> Unit = { viewModel.onSignUpClicked(it.signUpForm) },
    nameContent: @Composable (SignUpState) -> Unit = {
        CreateNameTextField(
            state = it.validationState,
            nameState = it.nameInputState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
                 it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    },
    emailContent: @Composable (SignUpState) -> Unit = {
        CreateEmailAddressTextField(
            state = it.validationState,
            emailState = it.emailInputState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
                it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    },
    passwordContent: @Composable (SignUpState) -> Unit = {
        CreatePasswordTextField(
            state = it.validationState,
            passwordState = it.passwordInputValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            it.localFocusRequester.moveFocus(FocusDirection.Down)
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
            ImageVector.vectorResource(id = R.drawable.ic_stich),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp),
        )

        Text(
            text = "Create your Account",
            style = MaterialTheme.typography.h3,
            modifier = modifier.padding(bottom = 12.dp)
        )

        SignUpValidationUI(
            signUpState,
            nameContent,
            emailContent,
            passwordContent,
            onSignUp
        )

        //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        val signUpText = buildAnnotatedString {
            append("Already have an account? ")
            withStyle(SpanStyle(color = MaterialTheme.colors.primary)) {
                append("Sign In")
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

@ExperimentalComposeUiApi
@Composable
private fun SignUpValidationUI(
    signUpState: SignUpState,
    nameContent: @Composable (SignUpState) -> Unit,
    emailContent: @Composable (SignUpState) -> Unit,
    passwordContent: @Composable (SignUpState) -> Unit,
    onSignUp: (SignUpState) -> Unit
) {
    nameContent(signUpState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    emailContent(signUpState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    passwordContent(signUpState)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    SignUpLoginButton(signUpState, onSignUp)
}

@Composable
private fun SignUpLoginButton(
    signUpState: SignUpState,
    onSignUp: (SignUpState) -> Unit
) {
    Button(
        onClick = { onSignUp(signUpState) },
        shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp)),
        contentPadding = PaddingValues(16.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        Text(text = "Sign up", textAlign = TextAlign.Center)
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
            text = "- Or sign up with -",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_fb),
                contentDescription = "Facebook icon",
                modifier = Modifier.clickable { }
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_google),
                contentDescription = "Google icon",
                modifier = Modifier.clickable { },
            )
        }
    }
}
