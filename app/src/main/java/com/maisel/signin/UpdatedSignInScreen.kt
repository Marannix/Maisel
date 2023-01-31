package com.maisel.signin

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.maisel.R
import com.maisel.common.composable.DefaultEmailContent
import com.maisel.common.composable.DefaultPasswordContent
import com.maisel.compose.state.onboarding.compose.AuthenticationFormState
import com.maisel.compose.state.onboarding.compose.AuthenticationState
import com.maisel.compose.ui.components.DefaultCallToActionButton
import com.maisel.compose.ui.components.OnboardingUserHeader
import com.maisel.compose.ui.components.onboarding.OnboardingAlternativeLoginFooter
import com.maisel.compose.ui.components.onboarding.ForgotPassword
import com.maisel.compose.ui.components.onboarding.OnboardingUserFooter
import com.maisel.compose.ui.theme.typography
import com.maisel.navigation.Screens
import com.maisel.ui.shapes
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun UpdatedSignInScreen(
    navHostController: NavHostController,
    viewModel: UpdatedSignInViewModel,
    uiState: SignInContract.SignInUiState,
    onClick: (SignInContract.SignInUiEvents) -> Unit,
) {


//    val authenticationState by viewModel.input.collectAsState()
//    val validationErrors by viewModel.validationErrors.collectAsState()

    //  val showErrorDialog: Boolean = viewState.authResultState is AuthResultState.Error

    val focusRequester = remember { FocusRequester() }
    val localFocusRequester = LocalFocusManager.current

    SignInContent(
        viewModel = viewModel,
        navHostController = navHostController,
        onGoogleClicked = { onClick(SignInContract.SignInUiEvents.GoogleButtonClicked) },
        onFacebookClicked = { onClick(SignInContract.SignInUiEvents.FacebookButtonClicked) },
        onForgotPasswordClicked = { onClick(SignInContract.SignInUiEvents.OnForgotPasswordClicked) },
        onSignUpClicked = { onClick(SignInContract.SignInUiEvents.SignUpButtonClicked) },
        // onLoginButtonClicked = { viewModel.onUiEvent(SignInContract.UiEvents.LoginButtonClicked) },
    )
}

@Composable
@ExperimentalComposeUiApi
fun SignInContent(
    viewModel: UpdatedSignInViewModel,
    navHostController: NavHostController,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val authResultLauncher = updatedManagedActivityResultGoogleSignIn(viewModel)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            LaunchedEffect(viewModel.snackbarMessage) {
                viewModel
                    .snackbarMessage
                    .collectLatest { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
            }

            LaunchedEffect(viewModel.screenDestinationName) {
                viewModel
                    .screenDestinationName
                    .collectLatest { screen ->
                        scope.launch {
                            when (screen.name == Screens.Dashboard.name) {
                                true -> navHostController.navigate(Screens.Dashboard.name) {
                                    popUpTo(Screens.Showcase.name) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                                false -> navHostController.navigate(screen.name)
                            }
                        }
                    }
            }

            LaunchedEffect(viewModel.launchGoogleSignIn) {
                viewModel
                    .launchGoogleSignIn
                    .collectLatest {
                        scope.launch {
                            authResultLauncher.launch(viewModel.getGoogleSignInClient().signInIntent)
                        }
                    }
            }

            Column(Modifier.fillMaxSize()) {
                UpdatedSignInMainCard(
                    viewModel = viewModel,
//                    authenticationState = AuthenticationState(
//                        validationErrors,
//                        showErrorDialog,
//                        authenticationState,
//                        focusRequester,
//                        localFocusRequester
//                    ),
                    onGoogleClicked = { onGoogleClicked() },
                    onFacebookClicked = { onFacebookClicked() },
                    onForgotPasswordClicked = { onForgotPasswordClicked() },
                    onSignUpClicked = { onSignUpClicked() },
                )
            }
        }
    )
}

@Composable
@ExperimentalComposeUiApi
fun UpdatedSignInMainCard(
    viewModel: UpdatedSignInViewModel,
//    authenticationState: AuthenticationState,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignInFormValueChange: (AuthenticationFormState) -> Unit = { viewModel.setSignInInput(it) },
    onSignIn: () -> Unit = {
        //   viewModel.onLoginClicked(authenticationState.authenticationFormState)
    },
    onLongPressed: () -> Unit = { },
    onSignUpClicked: () -> Unit,
    emailContent: @Composable (AuthenticationState) -> Unit = {
        DefaultEmailContent(
            state = it.validationState,
            value = it.authenticationFormState,
            onValueChange = onSignInFormValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    },
    passwordContent: @Composable (AuthenticationState) -> Unit = {
        DefaultPasswordContent(
            state = it.validationState,
            value = it.authenticationFormState,
            onValueChange = onSignInFormValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            it.localFocusRequester.moveFocus(FocusDirection.Down)
        }
    }, errorBanner: @Composable (AuthenticationState) -> Unit = {
        UpdatedSignInErrorBanner(
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

//        ValidationUI(
//            authenticationState,
//            modifier, //TODO: Delete this
//            onForgotPasswordClicked,
//            emailContent,
//            passwordContent,
//            errorBanner,
//            onSignIn
//        )

        //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        OnboardingAlternativeLoginFooter(onGoogleClicked, onFacebookClicked, "- Or sign in with -")

        OnboardingUserFooter("Don't have an account? ", "Sign up", onSignUpClicked)
    }
}

@ExperimentalComposeUiApi
@Composable
private fun ValidationUI(
    authenticationState: AuthenticationState,
    modifier: Modifier,
    onForgotPasswordClicked: () -> Unit,
    emailContent: @Composable (AuthenticationState) -> Unit,
    passwordContent: @Composable (AuthenticationState) -> Unit,
    errorBanner: @Composable (AuthenticationState) -> Unit,
    onSignIn: () -> Unit
) {
    errorBanner(authenticationState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    emailContent(authenticationState)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    passwordContent(authenticationState)
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    ForgotPassword("Forgot Password?", onForgotPasswordClicked, modifier)
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    DefaultCallToActionButton(onSignIn, "Sign in")
}

@Composable
fun UpdatedSignInErrorBanner(
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
                    style = typography.subtitle2,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun updatedManagedActivityResultGoogleSignIn(viewModel: UpdatedSignInViewModel) =
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


@Preview
@Composable
fun UpdatedSignInContentPreview() {

}
