package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.maisel.R
import com.maisel.dashboard.DashboardViewModel
import com.maisel.dashboard.RecentMessageState
import com.maisel.dashboard.chat.ChatsFragment
import com.maisel.domain.message.MessageModel
import com.maisel.domain.user.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
@ExperimentalComposeUiApi
fun RecentMessageList(
    padding: PaddingValues,
    viewModel: DashboardViewModel,
    listener: ChatsFragment.ChatsFragmentCallback?
) {
    val viewState by viewModel.viewState.collectAsState()
    val users by viewModel.users.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val latestMessages by viewModel.latestMessages.collectAsState(initial = emptyList())

    when (viewState.recentMessageState) {
        RecentMessageState.Loading -> {

        }
        is RecentMessageState.Success -> {
            Box(Modifier.fillMaxSize()) {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(items = (viewState.recentMessageState as RecentMessageState.Success).listOfMessages) { latestMessages ->
                        RecentMessageItem(listener, currentUser, users, latestMessages)
                    }
                }
            }
        }
        is RecentMessageState.Error -> {

        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun RecentMessageItem(
    listener: ChatsFragment.ChatsFragmentCallback?,
    currentUser: User,
    users: List<User>,
    messageModel: MessageModel
) {
    val chatPartnerId = if (messageModel.senderId == currentUser.userId) {
        messageModel.receiverId
    } else {
        messageModel.senderId
    }

    users.firstOrNull { it.userId == chatPartnerId }?.let { user ->
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .clickable { listener?.onOpenChatsDetails(user) }
                .padding(4.dp)
        ) {
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
                    .height(75.dp)
                    .width(75.dp)
                    .padding(start = 5.dp)
                    .padding(5.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    user.username.toString(),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )

                Row() {
                    if (messageModel.receiverId == currentUser.userId) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_double_tick),
                            contentDescription = "Read message indicator",
                            colorFilter = ColorFilter.tint(colorResource(R.color.maisel_compose_text_low_emphasis)),
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp)
                                .padding(top = 2.dp, bottom = 2.dp)
                        )
                    }

                    Text(
                        text = messageModel.message,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun <T> rememberFlow(
    flow: Flow<T>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
): Flow<T> {
    return remember(key1 = flow, key2 = lifecycleOwner) { flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED) }
}

@Composable
fun <T : R, R> Flow<T>.collectAsStateLifecycleAware(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext
): State<R> {
    val lifecycleAwareFlow = rememberFlow(flow = this)
    return lifecycleAwareFlow.collectAsState(initial = initial, context = context)
}

@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(
    context: CoroutineContext = EmptyCoroutineContext
): State<T> = collectAsStateLifecycleAware(value, context)
