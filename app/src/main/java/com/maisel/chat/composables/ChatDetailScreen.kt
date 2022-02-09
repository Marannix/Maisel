package com.maisel.chat.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.R
import com.maisel.chat.ChatDetailViewModel
import com.maisel.compose.ui.components.composers.MessageComposer
import com.maisel.compose.ui.components.shape.RecipientMessageBox
import com.maisel.compose.ui.components.shape.SenderMessageBox
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.domain.user.entity.SignUpUser
import com.maisel.message.MessageViewModel

@Composable
@ExperimentalComposeUiApi
@Preview(device = Devices.PIXEL_4)
fun ChatDetailScreen(
    viewModel: ChatDetailViewModel,
    messageViewModel: MessageViewModel,
    onBackButton: () -> Unit
) {
    val user: SignUpUser =
        viewModel.viewState.observeAsState().value?.user
            ?: throw Exception() //TODO: Handle this better

    Screen(viewModel, messageViewModel, user, onBackButton)
}

@ExperimentalComposeUiApi
@Composable
fun Screen(
    viewModel: ChatDetailViewModel,
    messageViewModel: MessageViewModel,
    user: SignUpUser,
    onBackButton: () -> Unit
) {

    val messageItems: List<MessageItem> =
        viewModel.viewState.observeAsState().value?.getMessagesItem() ?: emptyList()

    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    //   modifier = Modifier.navigationBarsWithImePadding().statusBarsPadding(), // Is this needed?
                    navigationIcon = {
                        // show drawer icon
                        IconButton(
                            onClick = {
                                result.value = "Back Arrow icon clicked"
                                onBackButton()
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                        }
                    },
                    title = {
                        Image(
                            painter = rememberImagePainter(
                                data = user.profilePicture ?: R.drawable.ic_son_goku,
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
                            user.username ?: "User",
                            style = ChatTheme.typography.h4,
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
                            result.value = " Video icon clicked"
                        }) {
                            Icon(
                                Icons.Rounded.Videocam,
                                contentDescription = ""
                            ) //TODO: Update asset
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
                    elevation = AppBarDefaults.TopAppBarElevation,
                    backgroundColor = ChatTheme.colors.barsBackground
                )
            },
            bottomBar = { MessageBox(messageViewModel) },
            content = { padding -> Content(padding, messageItems) }
        )
    }
}

@Composable
fun MessageBox(messageViewModel: MessageViewModel) {
    MessageComposer(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .navigationBarsWithImePadding(),
        messageViewModel = messageViewModel
    )
}

@Composable
fun Content(padding: PaddingValues, messageItems: List<MessageItem>) {
    Column(
        Modifier
            .fillMaxSize()
            .background(ChatTheme.colors.appBackground)
            .padding(padding)
            .padding(horizontal = 8.dp)
    ) {
        messageColumn(messageItems)
    }
}

//https://medium.com/nerd-for-tech/creating-a-heterogeneous-list-with-jetpack-compose-138d3698c4cc

@Composable
fun messageColumn(messageItems: List<MessageItem>) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messageItems) { item ->
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                when (item) {
                    is MessageItem.SenderMessageItem -> SenderCard(
                        state = item,
                        modifier = Modifier.fillMaxWidth(.85f)
                    )
                    is MessageItem.ReceiverMessageItem -> ReceiverCard(
                        state = item,
                        modifier = Modifier
                            .fillMaxWidth(.85f)
                            .padding(horizontal = 0.8.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun SenderCard(state: MessageItem.SenderMessageItem, modifier: Modifier) {
    SenderMessageBox(state)
}

@Composable
fun ReceiverCard(state: MessageItem.ReceiverMessageItem, modifier: Modifier) {
    RecipientMessageBox(state)
}
