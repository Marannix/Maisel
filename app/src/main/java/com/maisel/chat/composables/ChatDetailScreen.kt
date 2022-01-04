package com.maisel.chat.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.maisel.R
import com.maisel.compose.ui.components.composers.MessageComposer
import com.maisel.compose.ui.components.composers.MessageInput

@Composable
@ExperimentalComposeUiApi
@Preview(device = Devices.PIXEL_4)
fun ChatDetailScreen() {
    TopAppBar()
}

@ExperimentalComposeUiApi
@Composable
fun TopAppBar() {
    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
        topBar = {
            TopAppBar(
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
                title = {
                    Image(
                        painter = rememberImagePainter(
                            data = R.drawable.ic_son_goku,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.ic_son_goku) //TODO: Placeholder
                                transformations(CircleCropTransformation())
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .padding(start = 5.dp)
                            .padding(5.dp)
                    )

                    Text(
                        "User",
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
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
                        Icon(Icons.Rounded.Videocam, contentDescription = "") //TODO: Update asset
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

                            DropdownMenuItem(onClick = {
                                expanded.value = false
                                result.value = "Second item clicked"
                            }) {
                                Text("Second item")
                            }
                        }
                    }
                },
                elevation = AppBarDefaults.TopAppBarElevation
            )
        },
        bottomBar = { BottomAppBar() { MessageBox() } },
        content = { }
    )
}

@Composable
fun BottomAppBar() {
    val messageState = remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        modifier = Modifier.fillMaxSize(),
        value = messageState.value, onValueChange = {
            messageState.value = it
        },
        placeholder = {
            Text(text = "Enter your message")
        }
    )

}

@Composable
fun MessageBox() {
    MessageComposer(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        input = { inputState ->
            MessageInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7f)
                    .padding(start = 8.dp),
                messageComposerState = inputState,
                onValueChange = { },
                label = {
                    Row(
                        Modifier.wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Keyboard,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "Type something"
                        )
                    }
                }
            )
        }
    )
}
