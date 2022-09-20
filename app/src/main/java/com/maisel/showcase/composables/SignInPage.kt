package com.maisel.showcase.composables

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.maisel.R
import com.maisel.common.composable.DefaultEmailContent
import com.maisel.common.composable.DefaultPasswordContent
import com.maisel.compose.state.onboarding.compose.AuthenticationState
import com.maisel.compose.state.onboarding.compose.SignInState
import com.maisel.compose.ui.components.DefaultCallToActionButton
import com.maisel.compose.ui.components.OnboardingUserHeader
import com.maisel.compose.ui.components.onboarding.OnboardingAlternativeLoginFooter
import com.maisel.compose.ui.components.onboarding.ForgotPassword
import com.maisel.compose.ui.components.onboarding.OnboardingUserFooter
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.navigation.Destination
import com.maisel.signin.SignInViewModel
import com.maisel.state.AuthResultState
import com.maisel.ui.shapes
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun SignInPage(
    navHostController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel(),
    googleSignInClient: GoogleSignInClient = viewModel.getGoogleLoginAuth()
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val viewState by viewModel.state.collectAsState()
    val authenticationState by viewModel.input.collectAsState()
    val validationErrors by viewModel.validationErrors.collectAsState()

    val showErrorDialog: Boolean = viewState.authResultState is AuthResultState.Error

    val focusRequester = remember { FocusRequester() }
    val localFocusRequester = LocalFocusManager.current

    when (viewState.authResultState) {
        is AuthResultState.Success -> {
            navHostController.navigate(Destination.Dashboard.name)
        }
    }

    val authResultLauncher = managedActivityResultGoogleSignIn(viewModel)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            Column(Modifier.fillMaxSize()) {
                SignInMainCard(
                    viewModel = viewModel,
                    signInState = SignInState(
                        validationErrors,
                        showErrorDialog,
                        authenticationState,
                        focusRequester,
                        localFocusRequester
                    ),
                    onGoogleClicked = { authResultLauncher.launch(googleSignInClient.signInIntent) },
                    onFacebookClicked = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Not implemented yet"
                            )
                        }
                    },
                    onForgotPasswordClicked = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Not implemented yet"
                            )
                        }
                    },
                    onSignUpClicked = { },
                )
            }
        }
    )
}

@Composable
private fun managedActivityResultGoogleSignIn(viewModel: SignInViewModel) =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (result.data != null) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(intent)
                viewModel.onGoogleSignInActivityResult(task)
            }
        }
    }


@ExperimentalComposeUiApi
@Composable
fun SignInMainCard(
    viewModel: SignInViewModel,
    signInState: SignInState,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignInFormValueChange: (AuthenticationState) -> Unit = { viewModel.setSignInInput(it) },
    onSignIn: () -> Unit = { viewModel.onLoginClicked(signInState.authenticationState) },
    onLongPressed: () -> Unit = { viewModel.onLongPressed() },
    onSignUpClicked: () -> Unit,
    emailContent: @Composable (SignInState) -> Unit = {
        DefaultEmailContent(
            state = it.validationState,
            value = it.authenticationState,
            onValueChange = onSignInFormValueChange,
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
            value = it.authenticationState,
            onValueChange = onSignInFormValueChange,
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
                .clickable { onLongPressed() }
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
                    style = ChatTheme.typography.subtitle2,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
