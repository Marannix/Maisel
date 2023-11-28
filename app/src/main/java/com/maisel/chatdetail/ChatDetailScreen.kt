package com.maisel.chatdetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.R
import com.maisel.compose.ui.components.composers.MessageComposer
import com.maisel.compose.ui.components.shape.RecipientMessageBox
import com.maisel.compose.ui.components.shape.SenderMessageBox
import com.maisel.compose.ui.theme.*
import com.maisel.data.utils.DateFormatter
import com.maisel.domain.user.entity.User
import com.maisel.message.MessageViewModel
import com.maisel.ui.shapes
import java.util.*

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

    val user: User? = chatDetailViewModel.viewState.observeAsState().value?.recipient
    //   ?: throw Exception() //TODO: Handle this better

    user?.let { it ->
        Screen(navHostController, chatDetailViewModel, messageViewModel, it)
    }
}

@Composable
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun Screen(
    navHostController: NavHostController,
    chatDetailViewModel: ChatDetailViewModel,
    messageViewModel: MessageViewModel,
    user: User
) {

    val messageItems: List<MessageItem> =
        chatDetailViewModel.viewState.observeAsState().value?.getMessagesItem() ?: emptyList()

    val result = remember { mutableStateOf("") }
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
                                result.value = "Back Arrow icon clicked"
                                navHostController.navigateUp()
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
                            user.username ?: "User",
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
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary
                )
            },
            content = { padding -> Content(padding, messageItems) },
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
fun Content(padding: PaddingValues, messageItems: List<MessageItem>) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(padding)
            .padding(horizontal = 8.dp)
    ) {
        MessageColumn(messageItems)
    }
}

//https://medium.com/nerd-for-tech/creating-a-heterogeneous-list-with-jetpack-compose-138d3698c4cc
@ExperimentalFoundationApi
@Composable
fun MessageColumn(messageItems: List<MessageItem>) {
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
                stickyHeader {
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
