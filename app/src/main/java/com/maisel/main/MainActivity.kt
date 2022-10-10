package com.maisel.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.maisel.chatdetail.ChatDetailScreen
import com.maisel.common.BaseActivity
import com.maisel.dashboard.DashboardScreen
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.compose.ui.theme.MaiselTheme
import com.maisel.contacts.ContactScreen
import com.maisel.navigation.Screens
import com.maisel.placeholder.PlaceholderScreen
import com.maisel.showcase.ShowcaseScreen
import com.maisel.signin.SignInScreen
import com.maisel.signup.SignUpScreen

class MainActivity : BaseActivity() {

    private var isSplashScreen = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable support for Splash Screen API for
        // proper Android 12+ support
        //   handleSplashScreen()
        installSplashScreen()
//            .apply {
//            setKeepOnScreenCondition {
//                isSplashScreen.value
//            }
//        }

        setContent {
            val mainViewModel = hiltViewModel<MainActivityViewModel>()
            val hasSeenShowcase by mainViewModel.hasSeenShowcase.collectAsState(initial = false)

            val startDestination = getStartDestination(mainViewModel, hasSeenShowcase)

            val navController = rememberNavController()

            MaiselTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold() { scaffoldPadding ->
                        NavHost(
                            modifier = Modifier.padding(scaffoldPadding),
                            navController = navController,
                            startDestination = startDestination
                        ) {
//                                composable(Screens.Showcase.name) {
//                                    ShowcaseScreen(navHostController = navController)
//                                }
                            composable(Screens.SignIn.name) {
                                Surface {
                                    SignInScreen(
                                        navHostController = navController
                                    )
                                }
                            }
//                                composable(Screens.SignUp.name) {
//                                    Surface(color = ChatTheme.colors.appBackground) {
//                                        SignUpScreen(navHostController = navController)
//                                    }
//                                }
//                                composable(Screens.Dashboard.name) {
//                                    ProvideWindowInsets(
//                                        windowInsetsAnimationsEnabled = true,
//                                        consumeWindowInsets = true
//                                    ) {
//                                        Surface {
//                                            DashboardScreen(navHostController = navController)
//                                        }
//                                    }
//                                }
//                                composable(
//                                    "${Screens.ChatDetail.name}/{receiverId}",
//                                    arguments = listOf(navArgument("receiverId") {
//                                        type = NavType.StringType
//                                    })
//                                ) {
//                                    ProvideWindowInsets(
//                                        windowInsetsAnimationsEnabled = true,
//                                        consumeWindowInsets = true
//                                    ) {
//                                        ChatDetailScreen(navHostController = navController)
//                                    }
//                                }
//                                composable(Screens.Contact.name) {
//                                    ProvideWindowInsets(
//                                        windowInsetsAnimationsEnabled = true,
//                                        consumeWindowInsets = true
//                                    ) {
//                                        Surface {
//                                            ContactScreen(navHostController = navController)
//                                        }
//                                    }
//                                }
//                                composable(Screens.Placeholder.name) {
//                                    PlaceholderScreen(navHostController = navController)
//                                }
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
                Screens.Dashboard.name
            }
            hasSeenShowcase -> {
                Screens.SignIn.name
            }
            else -> {
                Screens.Showcase.name
            }
        }
        return Screens.SignIn.name
    }

    private fun signInWithFacebook() {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
    }

    private fun forgotPassword() {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
    }

//    private fun handleSplashScreen() {
//        lifecycleScope.launch(Dispatchers.Default) {
//            delay(3000)
//            isSplashScreen.value = false
//        }
//    }
}
