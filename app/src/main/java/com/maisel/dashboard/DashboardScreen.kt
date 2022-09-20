package com.maisel.dashboard

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
import com.maisel.compose.ui.components.dashboard.DashboardAppBar
import com.maisel.compose.ui.components.dashboard.DashboardDrawer
import com.maisel.compose.ui.components.dashboard.RecentMessageList
import com.maisel.navigation.Screens
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
                onClick = { navHostController.navigate(Screens.Contact.name) }
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
