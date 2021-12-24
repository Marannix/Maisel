package com.maisel.dashboard.chat.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.ImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.maisel.R
import com.maisel.domain.user.entity.SignUpUser

@Composable
@ExperimentalComposeUiApi
@Preview(device = Devices.PIXEL_4)
fun ChatsList(chats: List<SignUpUser>) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(chats) { chat ->
                ChatListItem(chat, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun ChatListItem(chat: SignUpUser, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .padding(4.dp)
    ) {
        Image(
            painter = rememberImagePainter(
                data = chat.profilePicture ?: R.drawable.ic_son_goku,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.ic_son_goku) //TODO: Placeholder
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .padding(start = 5.dp)
                .padding(5.dp)
        )
        Column {
            Text(
                chat.username.toString(),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            Text(
                "Last message",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
            )
        }
    }
}
