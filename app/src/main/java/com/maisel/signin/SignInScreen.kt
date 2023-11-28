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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.maisel.R
import com.maisel.common.composable.EmailContent
import com.maisel.common.composable.LoadingIndicator.FullScreenLoadingIndicator
import com.maisel.common.composable.PasswordContent
import com.maisel.compose.ui.components.DefaultCallToActionButton
import com.maisel.compose.ui.components.OnboardingUserHeader
import com.maisel.compose.ui.components.onboarding.ForgotPassword
import com.maisel.compose.ui.components.onboarding.OnboardingAlternativeLoginFooter
import com.maisel.compose.ui.components.onboarding.OnboardingUserFooter
import com.maisel.compose.ui.theme.MaiselTheme
import com.maisel.compose.ui.theme.typography
import com.maisel.navigation.Screens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun SignInScreen(
    navHostController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInContent(
        navHostController = navHostController,
        viewModel = viewModel,
        uiState = uiState,
        uiEvents = viewModel::onUiEvent,
    )
}

@Composable
@ExperimentalComposeUiApi
fun SignInContent(
    navHostController: NavHostController,
    viewModel: SignInViewModel,
    uiState: SignInContract.SignInUiState,
    uiEvents: (SignInContract.SignInUiEvents) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val authResultLauncher = ManagedActivityResultGoogleSignIn(viewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
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
//                                        popUpTo(Screens.SignIn.name) {
//                                            inclusive = true
//                                        }
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
                    SignInMainCard(
                        uiState = uiState,
                        uiEvents = uiEvents
                    )
                }
            }
        )
        if (uiState.isLoading) {
            FullScreenLoadingIndicator(
                currentStatusBarColor = MaterialTheme.colors.background
            )
        }
    }
}

@Composable
@ExperimentalComposeUiApi
fun SignInMainCard(
    uiState: SignInContract.SignInUiState,
    uiEvents: (SignInContract.SignInUiEvents) -> Unit,
    onGoogleClicked: () -> Unit = { uiEvents(SignInContract.SignInUiEvents.GoogleButtonClicked) },
    onFacebookClicked: () -> Unit = { uiEvents(SignInContract.SignInUiEvents.FacebookButtonClicked) },
    onForgotPasswordClicked: () -> Unit = { uiEvents(SignInContract.SignInUiEvents.OnForgotPasswordClicked) },
    onSignIn: () -> Unit = {
        uiEvents(
            SignInContract.SignInUiEvents.LoginButtonClicked(
                uiState.email.text,
                uiState.password.text
            )
        )
    },
    onLongPressed: () -> Unit = { },
    onSignUpClicked: () -> Unit = { uiEvents(SignInContract.SignInUiEvents.SignUpButtonClicked) },
    emailContent: @Composable () -> Unit = {
        EmailContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            emailState = uiState.email,
            onValueChange = { uiEvents(SignInContract.SignInUiEvents.EmailUpdated(it)) }
        )
    },
    passwordContent: @Composable () -> Unit = {
        PasswordContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            passwordState = uiState.password,
            onValueChange = { uiEvents(SignInContract.SignInUiEvents.PasswordUpdated(it)) }
        )
    },
    errorBanner: @Composable () -> Unit = {
        if (!uiState.errorMessage.isNullOrBlank()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = uiState.errorMessage,
                color = MaterialTheme.colors.onBackground,
                style = typography.subtitle2,
                fontWeight = FontWeight.SemiBold
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
                .clickable { onLongPressed() }
                .padding(vertical = 24.dp),
        )

        OnboardingUserHeader("Login to your Account", modifier.padding(bottom = 12.dp))

        ValidationUI(
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
    onForgotPasswordClicked: () -> Unit,
    emailContent: @Composable () -> Unit,
    passwordContent: @Composable () -> Unit,
    errorBanner: @Composable () -> Unit,
    onSignIn: () -> Unit
) {
    errorBanner()
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    emailContent()
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    passwordContent()
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    ForgotPassword("Forgot Password?", onForgotPasswordClicked)
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    DefaultCallToActionButton(onSignIn, "Sign in")
}

@Composable
private fun ManagedActivityResultGoogleSignIn(viewModel: SignInViewModel) =
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

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SignInContentPreview(
    @PreviewParameter(SignInPreviewParameterProvider::class) uiState: SignInContract.SignInUiState
) {
    MaiselTheme {
        SignInContent(
            navHostController = rememberNavController(),
            uiState = uiState,
            uiEvents = { },
            viewModel = hiltViewModel()
        )
    }
}
