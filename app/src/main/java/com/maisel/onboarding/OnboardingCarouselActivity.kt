package com.maisel.onboarding

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.common.BaseActivity
import com.maisel.dashboard.MainActivity
import com.maisel.onboarding.composables.OnboardingCarousel
import com.maisel.signin.SignInActivity
import com.maisel.ui.MainTheme

@ExperimentalAnimationApi
@ExperimentalPagerApi
class OnboardingCarouselActivity : BaseActivity() {

    private val viewModel: OnboardingCarouselViewModel by lazy {
        ViewModelProvider(this).get(
            OnboardingCarouselViewModel::class.java
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
                    OnboardingCarousel(::launchLoginActivity)
                }
            }
        }
    }

    private fun signInCurrentUser() {
        if (viewModel.isUserLoggedIn()) {
            MainActivity.createIntent(this).also { startActivity(it) }
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