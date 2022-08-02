package com.maisel.dashboard.chat.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.R
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.chat.ContactsFragment
import com.maisel.dashboard.chat.ContactsViewModel
import com.maisel.domain.user.entity.User

@Composable
@ExperimentalComposeUiApi
fun ContactList(
    viewModel: ContactsViewModel,
    listener: ContactsFragment.ContactsFragmentCallback?
) {
    val users by viewModel.users.collectAsState()

    val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = {
                        Text(
                            "Select contact",
                            style = ChatTheme.typography.h4,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                result.value = "Back Arrow icon clicked"
                                listener?.onContactsBackPressed()
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                        }
                    },
                    elevation = AppBarDefaults.TopAppBarElevation,
                    backgroundColor = ChatTheme.colors.barsBackground,
                    contentColor = ChatTheme.colors.onPrimaryAccent
                )
            },
            content = { padding ->
                ContactList(users, listener)
            }
        )
    }
}

@Composable
@ExperimentalComposeUiApi
private fun ContactList(
    users: List<User>,
    listener: ContactsFragment.ContactsFragmentCallback?,
) {
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
    listener: ContactsFragment.ContactsFragmentCallback?,
    user: User,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .clickable { listener?.onOpenChatsDetails(user, "contacts") }
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
                style = ChatTheme.typography.body1,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}
