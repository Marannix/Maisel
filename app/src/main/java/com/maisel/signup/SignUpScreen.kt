package com.maisel.signup

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.maisel.common.composable.LoadingIndicator
import com.maisel.common.composable.NameContent
import com.maisel.common.composable.PasswordContent
import com.maisel.compose.ui.components.DefaultCallToActionButton
import com.maisel.compose.ui.components.OnboardingUserHeader
import com.maisel.compose.ui.components.onboarding.OnboardingAlternativeLoginFooter
import com.maisel.compose.ui.components.onboarding.OnboardingUserFooter
import com.maisel.compose.ui.theme.MaiselTheme
import com.maisel.compose.ui.theme.typography
import com.maisel.navigation.Screens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun SignUpScreen(
    navHostController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignUpContent(
        navHostController = navHostController,
        viewModel = viewModel,
        uiState = uiState,
        uiEvents = viewModel::onUiEvent,
    )
}

@Composable
@ExperimentalComposeUiApi
fun SignUpContent(
    navHostController: NavHostController,
    viewModel: SignUpViewModel,
    uiState: SignUpContract.UiState,
    uiEvents: (SignUpContract.UiEvents) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val authResultLauncher = ManagedActivityResultGoogleSignUp(viewModel)

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

                LaunchedEffect(key1 = uiState.navigateToSignIn) {
                    if (uiState.navigateToSignIn) {
                        navHostController.navigateUp()
                    }
                }

                LaunchedEffect(key1 = Unit) {
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
                    SignUpMainCard(
                        uiState = uiState,
                        uiEvents = uiEvents
                    )
                }
            }
        )
        if (uiState.isLoading) {
            LoadingIndicator.FullScreenLoadingIndicator(
                currentStatusBarColor = MaterialTheme.colors.background
            )
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun SignUpMainCard(
    uiState: SignUpContract.UiState,
    uiEvents: (SignUpContract.UiEvents) -> Unit,
    onGoogleClicked: () -> Unit = { uiEvents(SignUpContract.UiEvents.GoogleButtonClicked) },
    onFacebookClicked: () -> Unit = { uiEvents(SignUpContract.UiEvents.FacebookButtonClicked) },
    onSignUp: () -> Unit = {
        uiEvents(
            SignUpContract.UiEvents.SignUpButtonClicked(
                uiState.name.text,
                uiState.email.text,
                uiState.password.text,
            )
        )
    },
//    onSignIn: () -> Unit = { navHostController.navigateUp() },
    onSignIn: () -> Unit = { uiEvents(SignUpContract.UiEvents.SignInButtonClicked) },
    nameContent: @Composable () -> Unit = {
        NameContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            nameState = uiState.name,
            onValueChange = { uiEvents(SignUpContract.UiEvents.NameUpdated(it)) }
        )
    },
    emailContent: @Composable () -> Unit = {
        EmailContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            emailState = uiState.email,
            onValueChange = { uiEvents(SignUpContract.UiEvents.EmailUpdated(it)) }
        )
    },
    passwordContent: @Composable () -> Unit = {
        PasswordContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            passwordState = uiState.password,
            onValueChange = { uiEvents(SignUpContract.UiEvents.PasswordUpdated(it)) }
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
            ImageVector.vectorResource(id = R.drawable.ic_stich),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp),
        )

        OnboardingUserHeader("Create your Account", modifier.padding(bottom = 12.dp))

        SignUpValidationUI(
            nameContent,
            emailContent,
            passwordContent,
            errorBanner,
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
    nameContent: @Composable () -> Unit,
    emailContent: @Composable () -> Unit,
    passwordContent: @Composable () -> Unit,
    errorBanner: @Composable () -> Unit,
    onSignUp: () -> Unit
) {
    errorBanner()
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    nameContent()
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    emailContent()
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    passwordContent()
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
    DefaultCallToActionButton(onSignUp, "Sign up")
}

@Composable
private fun ManagedActivityResultGoogleSignUp(viewModel: SignUpViewModel) =
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
fun SignUpContentPreview(
    @PreviewParameter(SignUpPreviewParameterProvider::class) uiState: SignUpContract.UiState
) {
    MaiselTheme {
        SignUpContent(
            navHostController = rememberNavController(),
            uiState = uiState,
            uiEvents = { },
            viewModel = hiltViewModel()
        )
    }
}
