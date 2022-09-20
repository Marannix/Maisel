package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.maisel.R
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardDrawerMenuItem
import com.maisel.dashboard.DashboardFragment
import com.maisel.dashboard.DashboardViewModel
import com.maisel.domain.user.entity.User

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun DashboardDrawer(
    viewModel: DashboardViewModel,
    listener: DashboardFragment.DashboardFragmentCallback?,
    header: @Composable () -> Unit = {
        DefaultComposerDrawerHeader(
            viewModel.currentUser.collectAsState(),
            listener
        )
    },
    body: @Composable () -> Unit = {
        DefaultComposerDrawerBody(
            items = viewModel.getMenuItems(),
            listener = listener
        )
    }
) {
    header()
    body()
}

@ExperimentalComposeUiApi
@Composable
internal fun DefaultComposerDrawerHeader(
    user: State<User>,
    listener: DashboardFragment.DashboardFragmentCallback?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ChatTheme.colors.barsBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = rememberImagePainter(
                    data = user.value.profilePicture
                        ?: R.drawable.ic_son_goku, //TODO: Need a default profile picture
                    builder = {
                        crossfade(true)
                   //     placeholder(R.drawable.ic_son_goku) //TODO: Need a default profile picture
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp)
                    .padding(start = 5.dp)
                    .padding(5.dp)
                    .clickable { listener?.onOpenSettings() }
            )

            Text(
                text = user.value.username ?: "",
                style = ChatTheme.typography.body2,
                color = ChatTheme.colors.onPrimaryAccent,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = user.value.emailAddress ?: "",
                style = ChatTheme.typography.body2,
                color = ChatTheme.colors.textLowEmphasis,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@ExperimentalComposeUiApi
@Composable
internal fun DefaultComposerDrawerBody(
    items: List<DashboardDrawerMenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = ChatTheme.typography.body2,
    listener: DashboardFragment.DashboardFragmentCallback?
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        listener?.onDrawerMenuItemClicked(item)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = item.icon, contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
