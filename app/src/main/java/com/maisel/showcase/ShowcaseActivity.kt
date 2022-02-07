package com.maisel.showcase

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.common.BaseActivity
import com.maisel.dashboard.DashboardActivity
import com.maisel.showcase.composables.Showcase
import com.maisel.signin.SignInActivity
import com.maisel.ui.MainTheme

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
        super.onCreate(savedInstanceState)
        signInCurrentUser()

        setContent {
            MainTheme {
                window.statusBarColor = MaterialTheme.colors.background.toArgb()
                window.navigationBarColor = MaterialTheme.colors.background.toArgb()

                Surface(
                    color = MaterialTheme.colors.background,
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

    private fun backButtonPressed() {
        notImplementedYet()
    }

    private fun skipPressed() {
        notImplementedYet()
    }

    private fun launchLoginActivity() {
        SignInActivity.createIntent(this).also { startActivity(it) }
        finish()
    }
}
