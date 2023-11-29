package com.maisel.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Message
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.maisel.compose.ui.components.dashboard.DashboardAppBar
import com.maisel.compose.ui.components.dashboard.DashboardDrawer
import com.maisel.compose.ui.components.dashboard.RecentMessageList
import com.maisel.navigation.Screens
import com.maisel.state.UserAuthState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun DashboardScreen(
    navHostController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.destination) {
        viewModel
            .destination
            .collectLatest { destination ->
                scope.launch {
                    when (destination) {
                        is DashboardDestination.ChatDetail -> {
                            navHostController.navigate(
                                route = "${Screens.ChatDetail.name}/${destination.receiverUserId}"
                            )
                        }
                    }
                }
            }
    }

    DashboardContent(
        navHostController = navHostController,
        viewModel = viewModel,
        uiState = uiState,
        uiEvents = viewModel::onUiEvent,
    )
}

@Composable
@ExperimentalComposeUiApi
private fun DashboardContent(
    navHostController: NavHostController,
    viewModel: DashboardViewModel,
    uiState: DashboardContract.UiState,
    uiEvents: (DashboardContract.UiEvents) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.fillMaxSize(),
            drawerContent = {
                DashboardDrawer(navHostController, viewModel)
            },
            topBar = {
                DashboardAppBar(result = result, expanded = expanded, onNavigationItemClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                })
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.navigationBarsWithImePadding(),
                    contentColor = MaterialTheme.colors.onSecondary,
                    backgroundColor = MaterialTheme.colors.secondary,
                    onClick = { navHostController.navigate(Screens.Contact.name) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Message,
                        contentDescription = "Navigate to Contacts Screen"
                    )
                }
            },
            content = {
                when (uiState.userAuthState) {
                    UserAuthState.LOGGED_OUT -> {
                        LaunchedEffect(key1 = uiState.userAuthState) {
                            navHostController.navigate(
                                route = Screens.SignIn.name,
                                navOptions = NavOptions
                                    .Builder()
                                    .setPopUpTo(Screens.Dashboard.name, true)
                                    .build()
                            )
                        }
//                    navHostController.navigate(Screens.SignIn.name) {
////                        navHostController.popBackStack(
////                            route = Screens.UpdatedSignIn.name,
////                            inclusive = false
////                        )
//                        launchSingleTop = true
//                        //     }
//                    }
                        //      navHostController.popBackStack()
                    }

                    else -> {
                        // DO NOTHING
                    }
                }

                if (uiState.currentUser != null) {
                    RecentMessageList(
                        users = uiState.contacts,
                        currentUser = uiState.currentUser,
                        recentMessageState = uiState.recentMessageState,
                        uiEvents = uiEvents
                    )
                }
            }
        )
    }
}
