@file:OptIn(ExperimentalComposeUiApi::class)

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.R
import com.maisel.compose.ui.theme.typography
import com.maisel.domain.user.entity.User
import com.maisel.navigation.Screens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ContactScreen(
    navHostController: NavHostController,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.destination) {
        viewModel
            .destination
            .collectLatest { destination ->
                scope.launch {
                    when (destination) {
                        is ContactsDestination.ChatDetail -> {
                            navHostController.navigate(
                                "${Screens.ChatDetail.name}/${destination.contactId}"
                            )
                        }
                    }
                }
            }
    }

    ContactContent(
        navHostController = navHostController,
        uiState = uiState,
        uiEvents = viewModel::onUiEvent,
    )
}

@Composable
private fun ContactContent(
    navHostController: NavHostController,
    uiState: ContactsContract.UiState,
    uiEvents: (ContactsContract.UiEvents) -> Unit,
) {
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
                ContactList(
                    users = uiState.contacts,
                    uiEvents = uiEvents
                )
            }
        )
    }

}

@Composable
@ExperimentalComposeUiApi
private fun ContactList(
    users: List<User>,
    uiEvents: (ContactsContract.UiEvents) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(users) { contact ->
                ChatListItem(
                    contact = contact,
                    uiEvents = uiEvents)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ChatListItem(
    contact: User,
    uiEvents: (ContactsContract.UiEvents) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uiEvents(ContactsContract.UiEvents.OnContactClicked(contact.userId))
            }
            .padding(4.dp)
    ) {
        Image(
            painter = rememberImagePainter(
                data = contact.profilePicture ?: R.drawable.ic_son_goku,
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
                contact.username.toString(),
                style = typography.body1,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}
