package com.maisel.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.common.BaseActivity
import com.maisel.dashboard.DashboardActivity
import com.maisel.showcase.composables.SignUpPage
import com.maisel.state.AuthResultState
import com.maisel.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class SignUpActivity : BaseActivity() {

    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(
            SignUpViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SignUpPage(viewModel)
                }
            }
        }

        observeViewState()
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: SignUpViewState) {
        when (state.authResultState) {
            AuthResultState.Error -> {
                makeToastShort("Error Signing Up")
            }
            AuthResultState.Loading -> {
               // makeToastShort("Error Signing Up")
            }
            is AuthResultState.Success -> {
                makeToastShort("Account Created")
                viewModel.setUser(state.authResultState.user)
                DashboardActivity.createIntent(this).also { startActivity(it) }
                finishAffinity()
            }
            AuthResultState.Idle -> {
                Log.d("joshua", "activity idle")
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}
