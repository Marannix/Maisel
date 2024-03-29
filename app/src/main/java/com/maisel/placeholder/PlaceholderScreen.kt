package com.maisel.placeholder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieConstants
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.compose.ui.components.lottie.LottieAsset

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
                        Text("Not Implemented Yet")
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
                    contentColor = MaterialTheme.colors.primary,
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
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Box(Modifier.fillMaxSize()) {
            LottieAsset("cat_sleeping.json", iterations = LottieConstants.IterateForever)
        }
    }
}
