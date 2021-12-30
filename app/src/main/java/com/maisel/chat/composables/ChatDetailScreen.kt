package com.maisel.chat.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.maisel.dashboard.DashboardViewModel

@Composable
@ExperimentalComposeUiApi
@Preview(device = Devices.PIXEL_4)
fun ChatDetailScreen() {
    TopAppBar()
}

@Composable
fun TopAppBar() {
    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Title")
                },

                navigationIcon = {
                    // show drawer icon
                    IconButton(
                        onClick = {
                            result.value = "Back Arrow icon clicked"
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                    }
                },

                actions = {
                    IconButton(onClick = {
                        result.value = " Call icon clicked"
                    }) {
                        Icon(Icons.Filled.Phone, contentDescription = "") //TODO: Update asset
                    }

                    IconButton(onClick = {
                        result.value = " Call icon clicked"
                    }) {
                        Icon(Icons.Filled.Call, contentDescription = "") //TODO: Update asset
                    }

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
                                result.value = "First item clicked"
                            }) {
                                Text("First Item")
                            }

                            Divider()

                            DropdownMenuItem(onClick = {
                                expanded.value = false
                                result.value = "Second item clicked"
                            }) {
                                Text("Second item")
                            }

                            Divider()

                        }
                    }
                },

                backgroundColor = Color(0xFDCD7F32),
                elevation = AppBarDefaults.TopAppBarElevation
            )
        },
        content = {

        }
    )
}


