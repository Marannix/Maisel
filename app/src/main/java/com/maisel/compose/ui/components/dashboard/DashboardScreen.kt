package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Message
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.maisel.dashboard.DashboardFragment
import com.maisel.dashboard.DashboardViewModel
import com.maisel.navigation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun DashboardScreen(
    navHostController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel(),
    listener: DashboardFragment.DashboardFragmentCallback? = null,
    drawer: @Composable ColumnScope.() -> Unit = {
        DashboardDrawer(
            viewModel,
            listener
        )
    }
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        DashboardScaffold(
            navHostController,
            scaffoldState,
            drawer,
            result,
            expanded,
            listener,
            scope
        )
    }
}

@Composable
@ExperimentalComposeUiApi
private fun DashboardScaffold(
    navHostController: NavHostController,
    scaffoldState: ScaffoldState,
    drawer: @Composable() (ColumnScope.() -> Unit),
    result: MutableState<String>,
    expanded: MutableState<Boolean>,
    listener: DashboardFragment.DashboardFragmentCallback?,
    scope: CoroutineScope
) {
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        drawerContent = drawer,
        topBar = {
            DashboardAppBar(result, expanded, listener, onNavigationItemClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.navigationBarsWithImePadding(),
                onClick = {  navHostController.navigate(Screens.Contact.name) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Message,
                    contentDescription = "Navigate to Contacts Screen"
                )
            }
        },
        content = {
            RecentMessageList(navHostController)
        }
    )
}
