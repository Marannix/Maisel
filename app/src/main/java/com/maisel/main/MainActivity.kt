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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
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
import com.maisel.domain.database.AppTheme
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.navigation.Screens
import com.maisel.placeholder.PlaceholderScreen
import com.maisel.setting.SettingsScreen
import com.maisel.showcase.ShowcaseScreen
import com.maisel.signin.SignInScreen
import com.maisel.signup.SignUpScreen
import com.maisel.utils.ThemeMapper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Added to fix keyboard backdrop issue in screen.
        window.decorView.setBackgroundResource(R.color.keyboard_background)

        val startDestination = requireNotNull(intent.getStringExtra(START_DESTINATIONS_KEY))
        val appTheme = mutableStateOf(intent.getStringExtra(APP_THEME_KEY))

        loadCache(appTheme)

        setContent {
            val navController = rememberNavController()

            MaiselTheme(
                appTheme = viewModel.getTheme(appTheme.value ?: AppTheme.SYSTEM_DEFAULT.name)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold { scaffoldPadding ->
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
                            composable(Screens.Settings.name) {
                                SettingsScreen(navHostController = navController)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadCache(appTheme: MutableState<String?>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.applicationCache.collectLatest { cache ->
                    when (cache) {
                        ApplicationCacheState.Error -> {
                        }

                        is ApplicationCacheState.Loaded -> {
                            appTheme.value = cache.settings.appTheme?.name
                        }

                        ApplicationCacheState.Loading -> {
                            //NO-OP
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val START_DESTINATIONS_KEY = "START_DESTINATIONS"
        const val APP_THEME_KEY = "APP_THEME"

        fun getCallingIntent(context: Context, screen: Screens, appTheme: String?): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(START_DESTINATIONS_KEY, screen.name)
                putExtra(APP_THEME_KEY, appTheme)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }
    }
}
