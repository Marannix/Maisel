package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Message
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardFragment
import com.maisel.dashboard.DashboardViewModel

@ExperimentalComposeUiApi
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    listener: DashboardFragment.DashboardFragmentCallback?,
    onBackButton: () -> Unit = { }
) {
    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = {
                        Text("Maisel",
                            style = ChatTheme.typography.h4,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                    },
                    actions = {
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
                                }) {
                                    Text("Logout")
                                }
                            }
                        }
                    },
                    elevation = AppBarDefaults.TopAppBarElevation,
                    backgroundColor = ChatTheme.colors.barsBackground
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { listener?.openContactsList() }
                ) {
                    Icon(imageVector = Icons.Rounded.Message, contentDescription = "Navigate to Contacts Screen")
                }
            },
            content = { padding ->
                RecentMessageList(padding, viewModel, null)
            }
        )
    }
}
