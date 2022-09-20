package com.maisel.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.maisel.R
import com.maisel.common.BaseActivity
import com.maisel.compose.ui.components.dashboard.DashboardScreen
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.navigation.Destination
import com.maisel.showcase.composables.Showcase
import com.maisel.showcase.composables.SignInPage
import com.maisel.showcase.composables.SignUpPage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private var isSplashScreen = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  WindowCompat.setDecorFitsSystemWindows(window, false)

        // Enable support for Splash Screen API for
        // proper Android 12+ support
        handleSplashScreen()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                isSplashScreen.value
            }
        }

        setContent {
            val mainViewModel = hiltViewModel<MainActivityViewModel>()
            val hasSeenShowcase by mainViewModel.hasSeenShowcase.collectAsState(initial = false)

            val startDestination = getStartDestination(mainViewModel, hasSeenShowcase)

            setContent {
                val navController = rememberNavController()

                ChatTheme {
                    window.statusBarColor = ChatTheme.colors.appBackground.toArgb()
                    window.navigationBarColor = ChatTheme.colors.appBackground.toArgb()

                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Scaffold() { scaffoldPadding ->
                            NavHost(
                                modifier = Modifier.padding(scaffoldPadding),
                                navController = navController,
                                startDestination = startDestination
                                //    startDestination = Destination.Showcase.name
                            ) {
                                composable(Destination.Dashboard.name) {
                                    Surface(color = ChatTheme.colors.appBackground) {

                                        ProvideWindowInsets(
                                            windowInsetsAnimationsEnabled = true,
                                            consumeWindowInsets = true
                                        ) {
                                            Surface {
                                                DashboardScreen(navHostController = navController)
                                            }
                                        }
                                    }
                                }
                                composable(Destination.Showcase.name) {
                                    Showcase(navHostController = navController)
                                }
                                composable(Destination.SignIn.name) {
                                    Surface(color = ChatTheme.colors.appBackground) {
                                        SignInPage(
                                            navHostController = navController
                                        )
                                    }
                                }
                                composable(Destination.SignUp.name) {
                                    Surface(color = ChatTheme.colors.appBackground) {
                                        SignUpPage(navHostController = navController)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun getStartDestination(
        mainViewModel: MainActivityViewModel,
        hasSeenShowcase: Boolean
    ): String {
        val startDestination = when {
            mainViewModel.isUserLoggedIn() -> {
                Destination.Dashboard.name
            }
            hasSeenShowcase -> {
                Destination.SignIn.name
            }
            else -> {
                Destination.Showcase.name
            }
        }
        return startDestination
    }

    private fun signInWithFacebook() {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
    }

    private fun forgotPassword() {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
    }

    private fun handleSplashScreen() {
        lifecycleScope.launch(Dispatchers.Default) {
            delay(3000)
            isSplashScreen.value = false
        }
    }
}
