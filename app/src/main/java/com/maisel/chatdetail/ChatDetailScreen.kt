package com.maisel.chatdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.R
import com.maisel.compose.ui.components.composers.MessageComposer
import com.maisel.compose.ui.components.shape.RecipientMessageBox
import com.maisel.compose.ui.components.shape.SenderMessageBox
import com.maisel.compose.ui.theme.extendedColors
import com.maisel.compose.ui.theme.typography
import com.maisel.data.utils.DateFormatter
import com.maisel.message.MessageViewModel
import com.maisel.ui.shapes
import java.util.Date

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun ChatDetailScreen(
    navHostController: NavHostController,
    chatDetailViewModel: ChatDetailViewModel = hiltViewModel(),
    messageViewModel: MessageViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        chatDetailViewModel.init()
        messageViewModel.init()
    }

    val chatDetailUiState by chatDetailViewModel.uiState.collectAsStateWithLifecycle()


    //val user: User? = chatDetailViewModel.viewState.observeAsState().value?.recipient
    //   ?: throw Exception() //TODO: Handle this better

    ChatDetailContent(
        messageViewModel = messageViewModel,
        chatDetailUiState = chatDetailUiState,
        chatDetailUiEvents = chatDetailViewModel::onUiEvent,
    )

}

@Composable
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun ChatDetailContent(
    //  chatDetailViewModel: ChatDetailViewModel,
    messageViewModel: MessageViewModel,
    chatDetailUiState: ChatDetailsContract.UiState,
    chatDetailUiEvents: (ChatDetailsContract.UiEvents) -> Unit,
    // user: User
) {

//    val messageItems: List<MessageItem> =
//        chatDetailViewModel.viewState.observeAsState().value?.getMessagesItem() ?: emptyList()

    val expanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    navigationIcon = {
                        // show drawer icon
                        IconButton(
                            onClick = {
                                chatDetailUiEvents(ChatDetailsContract.UiEvents.BackPressed)

                             //   navHostController.navigateUp()
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                        }
                    },
                    title = {
                        Image(
                            painter = rememberImagePainter(
                                data = chatDetailUiState.recipient?.profilePicture
                                    ?: R.drawable.ic_son_goku,
                                builder = {
                                    crossfade(true)
                                    //placeholder(R.drawable.ic_son_goku) //TODO: Placeholder
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
                            chatDetailUiState.recipient?.username ?: "",
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            chatDetailUiEvents(ChatDetailsContract.UiEvents.CallClicked)

                        }) {
                            Icon(Icons.Filled.Phone, contentDescription = "") //TODO: Update asset
                        }
                        IconButton(onClick = {
                            chatDetailUiEvents(ChatDetailsContract.UiEvents.VideoClicked)

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
                                }) {
                                    Text("First Item")
                                }

                                DropdownMenuItem(onClick = {
                                    expanded.value = false
                                }) {
                                    Text("Second item")
                                }
                            }
                        }
                    },
                    elevation = AppBarDefaults.TopAppBarElevation,
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary
                )
            },
            content = { padding -> ChatContent(padding, chatDetailUiState.messages) },
            bottomBar = { MessageBox(messageViewModel) }
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
@ExperimentalFoundationApi
fun ChatContent(padding: PaddingValues, messageItems: List<MessageItem>) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(padding)
            .padding(horizontal = 8.dp)
    ) {
        MessagesContent(messageItems)
    }
}

//https://medium.com/nerd-for-tech/creating-a-heterogeneous-list-with-jetpack-compose-138d3698c4cc
@ExperimentalFoundationApi
@Composable
fun MessagesContent(messageItems: List<MessageItem>) {
    val listState = rememberLazyListState()

    LaunchedEffect(messageItems.size) {
        listState.scrollToItem(messageItems.size)
    }

    Box(Modifier.fillMaxSize()) {
        val grouped = messageItems.groupBy { it.date }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            grouped.forEach { (section, message) ->
                item {
                    DayHeader(section)
                }
                items(message) { item ->
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    when (item) {
                        is MessageItem.SenderMessageItem -> SenderCard(
                            state = item
                        )

                        is MessageItem.ReceiverMessageItem -> ReceiverCard(
                            state = item,
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun SenderCard(state: MessageItem.SenderMessageItem) {
    SenderMessageBox(state)
}

@Composable
fun ReceiverCard(state: MessageItem.ReceiverMessageItem) {
    RecipientMessageBox(state)
}

@Composable
fun DayHeader(day: String) {
    val date = if (day == DateFormatter().getDate(Date().time)) {
        "Today"
    } else day

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(vertical = 4.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.extendedColors.borders),
                shape = RoundedCornerShape(24.dp)
            )
            .clip(shapes.medium.copy(CornerSize(24.dp)))
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = date,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = typography.subtitle2.copy(fontSize = 12.sp),
            textAlign = TextAlign.Center
        )
    }
}
