package com.maisel.dashboard

import android.graphics.drawable.Icon
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Message
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.maisel.compose.ui.components.dashboard.DashboardAppBar
import com.maisel.compose.ui.components.dashboard.DashboardDrawer
import com.maisel.compose.ui.components.dashboard.RecentMessageList
import com.maisel.navigation.Screens
import com.maisel.state.UserAuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun DashboardScreen(
    navHostController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel(),
    drawer: @Composable ColumnScope.() -> Unit = {
        DashboardDrawer(navHostController, viewModel)
    }
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        DashboardScaffold(
            navHostController,
            viewModel,
            scaffoldState,
            drawer,
            result,
            expanded,
            scope
        )
    }
}

@Composable
@ExperimentalComposeUiApi
private fun DashboardScaffold(
    navHostController: NavHostController,
    viewModel: DashboardViewModel,
    scaffoldState: ScaffoldState,
    drawer: @Composable() (ColumnScope.() -> Unit),
    result: MutableState<String>,
    expanded: MutableState<Boolean>,
    scope: CoroutineScope
) {
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        drawerContent = drawer,
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
            val viewState by remember(viewModel) { viewModel.viewState }.collectAsState()

                when (viewState.userAuthState) {
                    UserAuthState.LOGGED_OUT -> {
                        LaunchedEffect(key1 = viewState.userAuthState) {
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

            RecentMessageList(navHostController)
        }
    )
}
