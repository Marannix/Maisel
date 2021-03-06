package com.maisel.showcase.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.maisel.compose.ui.theme.ChatTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun Showcase(launchLoginActivity: () -> Unit) {
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {
        val items = ShowcaseItem.get()
        val state = rememberPagerState()

        ShowcaseTopSection(launchLoginActivity, state, scope)

        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            count = 3
        ) { page ->
            ShowcaseItem(item = items[page])
        }

        ShowcaseBottomSection(
            size = items.size,
            index = state.currentPage
        ) {
            if (state.currentPage + 1 < items.size) {
                scope.launch { //TODO: Crashed on Api 23
                    state.scrollToPage(state.currentPage + 1)
                }
            } else {
                launchLoginActivity()
            }
        }
    }
}

@Composable
fun ShowcaseBottomSection(size: Int, index: Int, onNextClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        ShowcaseShowcaseIndicators(size = size, index = index)

        //next button
        FloatingActionButton(
            onClick = onNextClicked,
            modifier = Modifier.align(Alignment.CenterEnd),
            backgroundColor = ChatTheme.colors.primaryAccent,
            contentColor = ChatTheme.colors.onPrimaryAccent
        ) {
            Icon(Icons.Outlined.KeyboardArrowRight, null)
        }
    }
}

@ExperimentalPagerApi
@Composable
fun ShowcaseTopSection(
    launchLoginActivity: () -> Unit,
    state: PagerState,
    scope: CoroutineScope
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        //back button
        IconButton(
            onClick = { goBack(state, scope)},
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.Outlined.KeyboardArrowLeft, null)
        }

        //skip button
        TextButton(
            onClick = { launchLoginActivity() },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text("Skip", color = ChatTheme.colors.onAppBackground)
        }
    }
}


@ExperimentalPagerApi
fun goBack(state: PagerState, scope: CoroutineScope) {
    if (state.currentPage != 0) {
        scope.launch {
            state.scrollToPage(state.currentPage - 1)
        }
    }
}

@Composable
fun BoxScope.ShowcaseShowcaseIndicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.align(Alignment.CenterStart)
    ) {
        repeat(size) {
            ShowcaseIndicator(isSelected = it == index)
        }
    }
}

@Composable
fun ShowcaseIndicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) ChatTheme.colors.primaryAccent else ChatTheme.colors.onAppBackground.copy(
                    alpha = 0.5f
                )
            )
    ) {

    }
}

@Composable
fun ShowcaseItem(item: ShowcaseItem) {
    val descriptionScrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Image(painter = painterResource(id = item.image), contentDescription = null, modifier = Modifier.weight(0.5f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .verticalScroll(descriptionScrollState)
                .weight(0.5f)
                .padding(16.dp),
        ) {
            Text(
                text = item.title,
                style = ChatTheme.typography.h2,
                color = ChatTheme.colors.onAppBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = item.text,
                style = ChatTheme.typography.body1,
                color = ChatTheme.colors.onAppBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
