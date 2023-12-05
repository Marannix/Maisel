package com.maisel.splash.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.maisel.R
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.main.MainActivity
import com.maisel.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * https://developersancho.medium.com/jetpack-compose-splash-screen-api-36ca40c6196b
 * https://medium.com/androiddevelopers/repeatonlifecycle-api-design-story-8670d1a7d333
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private var screen = Screens.Showcase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.applicationCache.collectLatest { cache ->
                    when (cache) {
                        ApplicationCacheState.Error -> {
                            screen = Screens.Showcase
                            navigateToMainActivity(screen)

                        }

                        is ApplicationCacheState.Loaded -> {
                            screen = when {
                                cache.settings.user != null -> {
                                    Screens.Dashboard
                                }

                                cache.settings.hasSeenShowcase -> {
                                    Screens.SignIn
                                }

                                else -> {
                                    Screens.Showcase
                                }
                            }
                            navigateToMainActivity(screen)
                        }

                        ApplicationCacheState.Loading -> {
                            //NO-OP
                        }
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
    }

    private fun navigateToMainActivity(screen: Screens) {
        val intent = MainActivity.getCallingIntent(applicationContext, screen)
        startActivity(intent)
        finish()
    }
}
