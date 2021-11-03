package com.maisel.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.maisel.common.BaseActivity
import com.maisel.dashboard.MainActivity
import com.maisel.dashboard.composables.OnboardingTut
import com.maisel.signin.SignInActivity
import com.maisel.signin.SignInViewModel
import com.maisel.signin.SignInViewState
import com.maisel.state.AuthResultState
import com.maisel.ui.OnBoardingTheme

@ExperimentalAnimationApi
@ExperimentalPagerApi
class OnboardingActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 65

    private val viewModel: SignInViewModel by lazy {
        ViewModelProvider(this).get(
            SignInViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            val showEmailError = viewModel.viewState.observeAsState().value?.signInValidator?.showEmailError ?: false
//            MainTheme {
//                Surface(color = MaterialTheme.colors.background) {
//                    //LoginPage(onLoginClicked = { beenTapped() })
//                    SignUpPage(viewModel, showEmailError)
//                    observeViewState()
//                }
//            }
//        }
        setContent {
            OnBoardingTheme {
                window.statusBarColor = MaterialTheme.colors.background.toArgb()
                window.navigationBarColor = MaterialTheme.colors.background.toArgb()

                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    OnboardingTut()
                }
            }
        }
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: SignInViewState) {
        when (state.authResultState) {
            AuthResultState.Idle -> {
                Log.d("joshua", "activity idle")
            }
            AuthResultState.Loading -> {
                //binding.signInButton.setLoading()
                Log.d("joshua", "activity loading")
            }
            is AuthResultState.Success -> {
                viewModel.setUser(state.authResultState.user)
             //   binding.signInButton.setComplete()
                Log.d("joshua", "activity success")
                MainActivity.createIntent(this).also { startActivity(it) }
                finish()
            }
            AuthResultState.Error -> {
              //  binding.signInButton.setFailed()
                Log.d("joshua", "activity error")
            }
        }

        state.signInValidator.showEmailError.let { showEmailError ->
//            if (showEmailError) {
//                binding.editTextInputEmailLayout.error = "Please enter a valid email"
//            }
//            binding.editTextInputEmailLayout.isErrorEnabled = showEmailError
        }
    }

    private fun beenTapped() {
        Toast.makeText(this, "tapped", Toast.LENGTH_SHORT).show()
    }
}