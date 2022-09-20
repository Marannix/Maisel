package com.maisel.placeholder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.compose.ui.components.lottie.LottieAsset
import com.maisel.compose.ui.theme.ChatTheme

/**
 * Represents that a screen is not implemented yet
 */
@Composable
fun PlaceholderScreen(
    navHostController: NavHostController
) {

    val result = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = {
                        Text(
                            "Not Implemented Yet",
                            style = ChatTheme.typography.h4,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
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
                    backgroundColor = ChatTheme.colors.barsBackground,
                    contentColor = ChatTheme.colors.onPrimaryAccent
                )
            },
            content = {
                PlaceholderDisguise()
            }
        )
    }
}

@Composable
fun PlaceholderDisguise() {
    Surface(modifier = Modifier.fillMaxSize(), color = ChatTheme.colors.appBackground) {
        Box(Modifier.fillMaxSize()) {
            LottieAsset("placeholder_error_screen.json")
        }
    }
}
