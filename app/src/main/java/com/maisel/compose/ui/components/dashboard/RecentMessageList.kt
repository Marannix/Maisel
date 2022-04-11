package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.maisel.R
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardFragment
import com.maisel.dashboard.DashboardViewModel
import com.maisel.dashboard.RecentMessageState
import com.maisel.domain.message.ChatModel
import com.maisel.domain.user.entity.User

@Composable
@ExperimentalComposeUiApi
fun RecentMessageList(
    viewModel: DashboardViewModel,
    listener: DashboardFragment.DashboardFragmentCallback?
) {
    val viewState by viewModel.viewState.collectAsState()
    val users by viewModel.users.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

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
    listener: DashboardFragment.DashboardFragmentCallback?,
    currentUser: User,
    users: List<User>,
    messageModel: ChatModel
) {
    users.firstOrNull { it.userId == messageModel.userId }?.let { receiverUser ->
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .clickable { listener?.onOpenChatsDetails(receiverUser) }
                .padding(4.dp)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = receiverUser.profilePicture ?: R.drawable.ic_son_goku,
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
                    receiverUser.username.toString(),
                    style = ChatTheme.typography.body1,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )

                Row {
                    if (messageModel.senderId == currentUser.userId) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_double_tick),
                            contentDescription = "Read message indicator",
                            colorFilter = ColorFilter.tint(colorResource(R.color.maisel_compose_text_low_emphasis)),
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp)
                                .padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp)
                        )
                    }

                    Text(
                        text = messageModel.message,
                        style = ChatTheme.typography.subtitle2,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
