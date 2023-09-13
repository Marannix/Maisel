package com.maisel.contacts

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.R
import com.maisel.compose.ui.theme.typography
import com.maisel.domain.user.entity.User
import com.maisel.navigation.Screens

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun ContactScreen(
    navHostController: NavHostController,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val result =
        remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = {
                        Text("Select contact")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                result.value = "Back Arrow icon clicked"
                                navHostController.navigateUp()
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                        }
                    },
                    elevation = AppBarDefaults.TopAppBarElevation,
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary
                )
            },
            content = {
                ContactList(users, navHostController)
            }
        )
    }
}

@Composable
@ExperimentalComposeUiApi
private fun ContactList(
    users: List<User>,
    navHostController: NavHostController
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(users) { user ->
                ChatListItem(navHostController, user)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ChatListItem(
    navHostController: NavHostController,
    user: User,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navHostController.navigate(
                    "${Screens.ChatDetail.name}/${user.userId}"
                )
            }
            .padding(4.dp)
    ) {
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
                .height(75.dp)
                .width(75.dp)
                .padding(start = 5.dp)
                .padding(5.dp)
        )
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                user.username.toString(),
                style = typography.body1,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}
