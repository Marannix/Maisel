package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.dashboard.DashboardContract

@ExperimentalComposeUiApi
@Composable
fun DashboardAppBar(
    uiEvents: (DashboardContract.UiEvents) -> Unit,
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body1,
                text = "Maisel",
                color = MaterialTheme.colors.onBackground,
            )
        },
        actions =
        {
            Box(
                Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = {
                    uiEvents(DashboardContract.UiEvents.SettingsClicked)
                }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = ""
                    )
                }
            }
        },
        elevation = AppBarDefaults.TopAppBarElevation,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
    )
}
