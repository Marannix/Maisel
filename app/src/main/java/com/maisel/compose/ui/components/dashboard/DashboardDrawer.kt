package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.maisel.R
import com.maisel.compose.ui.theme.extendedColors
import com.maisel.compose.ui.theme.typography
import com.maisel.dashboard.DashboardDrawerMenuItem
import com.maisel.dashboard.DashboardViewModel
import com.maisel.domain.user.entity.User

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun DashboardDrawer(
    navHostController: NavHostController,
    viewModel: DashboardViewModel,
    header: @Composable () -> Unit = {
//        DefaultComposerDrawerHeader(
//            viewModel.currentUser.collectAsState()
//        )
    },
    body: @Composable () -> Unit = {
        DefaultComposerDrawerBody(
            navHostController = navHostController,
            items = viewModel.getMenuItems()
        )
    }
) {
    header()
    body()
}

@ExperimentalComposeUiApi
@Composable
internal fun DefaultComposerDrawerHeader(
    user: State<User>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background),
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
                    .clickable { }
            )

            Text(
                text = user.value.username ?: "",
                style = typography.body2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = user.value.emailAddress ?: "",
                style = typography.body2,
                color = MaterialTheme.extendedColors.lowEmphasis,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@ExperimentalComposeUiApi
@Composable
internal fun DefaultComposerDrawerBody(
    navHostController: NavHostController,
    items: List<DashboardDrawerMenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = typography.body2
) {
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        LazyColumn(modifier = modifier) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navHostController.navigate(item.screen.name)
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

}
