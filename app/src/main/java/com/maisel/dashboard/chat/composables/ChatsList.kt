package com.maisel.dashboard.chat.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.maisel.R
import com.maisel.chat.composables.MessageItem
import com.maisel.dashboard.DashboardViewModel
import com.maisel.dashboard.chat.ChatsFragment
import com.maisel.domain.user.entity.SignUpUser

@Composable
@ExperimentalComposeUiApi
@Preview(device = PIXEL_4)
fun ChatsList(
    viewModel: DashboardViewModel,
    listener: ChatsFragment.ChatsFragmentCallback?,
) {
    val users by viewModel.users.collectAsState()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(users) { user ->
                ChatListItem(listener, user)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ChatListItem(
    listener: ChatsFragment.ChatsFragmentCallback?,
    user: SignUpUser,
) {

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
            Text(
                user.lastMessage.toString(),
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
                maxLines = 1
            )
        }
    }
}
