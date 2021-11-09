package com.maisel.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.common.BaseActivity
import com.maisel.dashboard.MainActivity
import com.maisel.onboarding.composables.SignUpPage
import com.maisel.state.AuthResultState
import com.maisel.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

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
            val showNameError =
                viewModel.viewState.observeAsState().value?.signUpValidator?.showNameError ?: false
            val showEmailError =
                viewModel.viewState.observeAsState().value?.signUpValidator?.showEmailError ?: false
            val showPasswordError =
                viewModel.viewState.observeAsState().value?.signUpValidator?.showPasswordError ?: false

            MainTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SignUpPage(viewModel, showNameError, showEmailError, showPasswordError)
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
                MainActivity.createIntent(this).also { startActivity(it) }
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