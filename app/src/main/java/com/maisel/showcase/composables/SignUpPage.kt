package com.maisel.showcase.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.maisel.R
import com.maisel.common.composable.DefaultEmailContent
import com.maisel.common.composable.DefaultNameContent
import com.maisel.common.composable.DefaultPasswordContent
import com.maisel.compose.state.onboarding.compose.AuthenticationState
import com.maisel.compose.state.onboarding.compose.SignUpState
import com.maisel.compose.ui.components.DefaultCallToActionButton
import com.maisel.compose.ui.components.OnboardingUserHeader
import com.maisel.compose.ui.components.onboarding.OnboardingAlternativeLoginFooter
import com.maisel.compose.ui.components.onboarding.OnboardingUserFooter
import com.maisel.navigation.Screens
import com.maisel.signup.SignUpViewModel
import com.maisel.state.AuthResultState

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun SignUpPage(
    navHostController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
    onGoogleClicked: () -> Unit = { },
    onFacebookClicked: () -> Unit = { }
) {

    val viewState by viewModel.state.collectAsState()
    val authenticationState by viewModel.input.collectAsState()
    val validationErrors by viewModel.validationErrors.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val localFocusRequester = LocalFocusManager.current

    val showErrorDialog: Boolean = viewState.authResultState is AuthResultState.Error

    when (viewState.authResultState) {
        is AuthResultState.Success -> {
            navHostController.navigate(Screens.Dashboard.name) {
                popUpTo(Screens.Showcase.name) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        SignUpMainCard(
            viewModel = viewModel,
            signUpState = SignUpState(
                validationErrors,
                showErrorDialog,
                authenticationState,
                focusRequester,
                localFocusRequester
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
    onSignUp: () -> Unit = { viewModel.onSignUpClicked(signUpState.authenticationState) },
    onSignUpFormValueChange: (AuthenticationState) -> Unit = { viewModel.setSignUpInput(it) },
    onSignIn: () -> Unit = { },
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    nameContent: @Composable (SignUpState) -> Unit = {
        DefaultNameContent(
            state = it.validationState,
            value = it.authenticationState,
            onValueChange = onSignUpFormValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    },
    emailContent: @Composable (SignUpState) -> Unit = {
        DefaultEmailContent(
            state = it.validationState,
            value = it.authenticationState,
            onValueChange = onSignUpFormValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    },
    passwordContent: @Composable (SignUpState) -> Unit = {
        DefaultPasswordContent(
            state = it.validationState,
            value = it.authenticationState,
            onValueChange = onSignUpFormValueChange,
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

        OnboardingUserHeader("Create your Account", modifier.padding(bottom = 12.dp))

        SignUpValidationUI(
            signUpState,
            nameContent,
            emailContent,
            passwordContent,
            onSignUp
        )

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
