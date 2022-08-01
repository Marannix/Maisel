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
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardFragment

@ExperimentalComposeUiApi
@Composable
fun DashboardAppBar(
    result: MutableState<String>,
    expanded: MutableState<Boolean>,
    listener: DashboardFragment.DashboardFragmentCallback?,
    onNavigationItemClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title =
        {
            Text(
                "Maisel",
                style = ChatTheme.typography.h4,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        },
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
                        listener?.onLogOut()
                    }) {
                        Text("Logout")
                    }
                }
            }
        },
        elevation = AppBarDefaults.TopAppBarElevation,
        backgroundColor = ChatTheme.colors.barsBackground,
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
