package com.maisel.compose.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardDrawerMenuItem
import com.maisel.dashboard.DashboardFragment

@ExperimentalComposeUiApi
@Composable
fun DashboardDrawer(
    items: List<DashboardDrawerMenuItem>,
    listener: DashboardFragment.DashboardFragmentCallback?,
    header: @Composable () -> Unit = { DefaultComposerDrawerHeader() },
    body: @Composable () -> Unit = { DefaultComposerDrawerBody(items = items, listener = listener) }
) {
    header()
    body()
}


@Composable
internal fun DefaultComposerDrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ChatTheme.colors.barsBackground)
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Header",
            color = ChatTheme.colors.textLowEmphasis,
            fontSize = 60.sp
        )
    }
}

@ExperimentalComposeUiApi
@Composable
internal fun DefaultComposerDrawerBody(
    items: List<DashboardDrawerMenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
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
