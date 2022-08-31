package com.maisel.showcase

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.common.BaseActivity
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardActivity
import com.maisel.showcase.composables.Showcase
import com.maisel.signin.SignInActivity

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
class ShowcaseActivity : BaseActivity() {

    private val viewModel: ShowcaseViewModel by lazy {
        ViewModelProvider(this).get(
            ShowcaseViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable support for Splash Screen API for
        // proper Android 12+ support
        installSplashScreen()

        super.onCreate(savedInstanceState)

        signInCurrentUser()

        setContent {
            ChatTheme {
                window.statusBarColor = ChatTheme.colors.appBackground.toArgb()
                window.navigationBarColor = ChatTheme.colors.appBackground.toArgb()

                Surface(
                    color = ChatTheme.colors.appBackground,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Showcase(::launchLoginActivity)
                }
            }
        }
    }

    private fun signInCurrentUser() {
        if (viewModel.isUserLoggedIn()) {
            DashboardActivity.createIntent(this).also { startActivity(it) }
            finish()
        }
    }

    private fun launchLoginActivity() {
        SignInActivity.createIntent(this).also { startActivity(it) }
        finish()
    }
}
