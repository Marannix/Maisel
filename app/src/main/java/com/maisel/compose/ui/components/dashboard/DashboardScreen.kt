package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Message
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.maisel.dashboard.DashboardFragment
import com.maisel.dashboard.DashboardViewModel
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    listener: DashboardFragment.DashboardFragmentCallback?,
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
                    onClick = { listener?.openContactsList() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Message,
                        contentDescription = "Navigate to Contacts Screen"
                    )
                }
            },
            content = { padding ->
                RecentMessageList(viewModel, listener)
            }
        )
    }
}
