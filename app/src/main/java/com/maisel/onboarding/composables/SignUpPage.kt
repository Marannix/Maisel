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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.maisel.signup.SignUpViewModel

@Composable
@Preview(device = Devices.PIXEL_4)
fun SignUpPage(
    viewModel: SignUpViewModel,
    showNameError: Boolean,
    showEmailError: Boolean,
    showPasswordError: Boolean
) {
    Column(Modifier.fillMaxSize()) {
        SignUpMainCard(viewModel, showNameError, showEmailError, showPasswordError)
    }
}

@Composable
fun SignUpMainCard(viewModel: SignUpViewModel, showNameError: Boolean, showEmailError: Boolean, showPasswordError: Boolean) {
    val nameState = remember { mutableStateOf(TextFieldValue("")) }
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
            viewModel,
            nameState,
            emailState,
            passwordState,
            modifier,
            focusRequester,
            showNameError,
            showEmailError,
            showPasswordError
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SignUpValidationUI(
    viewModel: SignUpViewModel,
    nameState: MutableState<TextFieldValue>,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    modifier: Modifier,
    focusRequester: FocusRequester,
    showNameError: Boolean,
    showEmailError: Boolean,
    showPasswordError: Boolean
)
{
    CreateNameTextField(nameState, showNameError, modifier, focusRequester)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    CreateEmailAddressTextField(emailState, showEmailError, modifier, focusRequester)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    CreatePasswordTextField(passwordState, showPasswordError, modifier, focusRequester)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    SignUpLoginButton(viewModel, nameState, emailState, passwordState, modifier)
}

@Composable
private fun SignUpLoginButton(
    viewModel: SignUpViewModel,
    nameState: MutableState<TextFieldValue>,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    modifier: Modifier
) {
    Button(
        onClick = { viewModel.onSignUpClicked(nameState, emailState, passwordState) },
        shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp)),
        contentPadding = PaddingValues(16.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 8.dp),
        modifier = modifier.padding(top = 8.dp)
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
