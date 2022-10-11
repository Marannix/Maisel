package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.compose.ui.theme.extendedColors
import com.maisel.compose.ui.theme.typography
import com.maisel.dashboard.DashboardViewModel

@ExperimentalComposeUiApi
@Composable
fun DashboardAppBar(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    result: MutableState<String>,
    expanded: MutableState<Boolean>,
    onNavigationItemClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = { Text(text = "Maisel") },
        actions =
        {
            Box(
                Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = {
                    expanded.value = true
                    result.value = "More icon clicked"
                }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = ""
                    )
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                ) {
                    DropdownMenuItem(onClick = {
                        expanded.value = false
                        result.value = "Setting clicked"
                    }) {
                        Text("Setting")
                    }

                    DropdownMenuItem(onClick = {
                        expanded.value = false
                        result.value = "Logout clicked"
                        dashboardViewModel.logOutUser()
                    }) {
                        Text("Logout")
                    }
                }
            }
        },
        elevation = AppBarDefaults.TopAppBarElevation,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        navigationIcon =
        {
            IconButton(onClick = {
                onNavigationItemClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        }
    )
}
