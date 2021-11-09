package com.maisel.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.maisel.R
import com.maisel.common.BaseActivity
import com.maisel.dashboard.MainActivity
import com.maisel.onboarding.composables.SignInPage
import com.maisel.signup.SignUpActivity
import com.maisel.state.AuthResultState
import com.maisel.ui.MainTheme

@ExperimentalAnimationApi
@ExperimentalPagerApi
class SignInActivity : BaseActivity() {

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
        signInCurrentUser()

        setContent {
            val showEmailError =
                viewModel.viewState.observeAsState().value?.signInValidator?.showEmailError ?: false
            val showErrorDialog =
                viewModel.viewState.observeAsState().value?.authResultState is AuthResultState.Error
            MainTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SignInPage(
                        viewModel,
                        showEmailError,
                        showErrorDialog,
                        ::signInWithGoogle,
                        ::signInWithFacebook,
                        ::forgotPassword,
                        ::navigateToSignUp
                    )
                }
            }
        }

        observeViewState()

        createGoogleSignInClient()
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
                Log.d("joshua", "activity loading")
            }
            is AuthResultState.Success -> {
                viewModel.setUser(state.authResultState.user)
                Log.d("joshua", "activity success")
                MainActivity.createIntent(this).also { startActivity(it) }
                finish()
            }
            AuthResultState.Error -> {
                Log.d("joshua", "activity error")
            }
        }
    }

    /**
     * TODO: Create splash screen
     *  Move this method to splash screen
     */
    private fun signInCurrentUser() {
        if (viewModel.isUserLoggedIn()) {
            MainActivity.createIntent(this).also { startActivity(it) }
            finish()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signInWithFacebook() {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
    }

    private fun forgotPassword() {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
    }

    private fun createGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun navigateToSignUp() {
        SignUpActivity.createIntent(this).also { startActivity(it) }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        viewModel.signInWithCredential(idToken, GoogleAuthProvider.getCredential(idToken, null))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }
}