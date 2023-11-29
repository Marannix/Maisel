package com.maisel.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.maisel.R
import com.maisel.chatdetail.ChatDetailScreen
import com.maisel.common.base.BaseActivity
import com.maisel.compose.ui.theme.MaiselTheme
import com.maisel.contacts.ContactScreen
import com.maisel.dashboard.DashboardScreen
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.navigation.Screens
import com.maisel.placeholder.PlaceholderScreen
import com.maisel.showcase.ShowcaseScreen
import com.maisel.signin.SignInScreen
import com.maisel.signup.SignUpScreen
import com.maisel.splash.ui.SplashScreenViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Added to fix keyboard backdrop issue in screen.
        window.decorView.setBackgroundResource(R.color.keyboard_background)

        val startDestination = requireNotNull(intent.getStringExtra(START_DESTINATIONS_KEY))

        setContent {
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
                            composable(Screens.Showcase.name) {
                                ShowcaseScreen(navHostController = navController)
                            }
                            composable(Screens.SignIn.name) {
                                SignInScreen(navHostController = navController)
                            }
                            composable(Screens.SignUp.name) {
                                SignUpScreen(navHostController = navController)

                            }
                            composable(Screens.Dashboard.name) {
                                ProvideWindowInsets(
                                    windowInsetsAnimationsEnabled = true,
                                    consumeWindowInsets = true
                                ) {
                                    DashboardScreen(navHostController = navController)
                                }
                            }
                            composable(
                                "${Screens.ChatDetail.name}/{receiverId}",
                                arguments = listOf(navArgument("receiverId") {
                                    type = NavType.StringType
                                })
                            ) {
                                ProvideWindowInsets(
                                    windowInsetsAnimationsEnabled = true,
                                    consumeWindowInsets = true
                                ) {
                                    ChatDetailScreen(navHostController = navController)
                                }
                            }
                            composable(Screens.Contact.name) {
                                ProvideWindowInsets(
                                    windowInsetsAnimationsEnabled = true,
                                    consumeWindowInsets = true
                                ) {
                                    Surface {
                                        ContactScreen(navHostController = navController)
                                    }
                                }
                            }
                            composable(Screens.Placeholder.name) {
                                PlaceholderScreen(navHostController = navController)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val START_DESTINATIONS_KEY = "START_DESTINATIONS"

        fun getCallingIntent(context: Context, screen: Screens): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(START_DESTINATIONS_KEY, screen.name)
            return intent
        }
    }
}
