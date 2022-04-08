package com.maisel.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.maisel.R
import com.maisel.common.BaseActivity
import com.maisel.dashboard.DashboardActivity
import com.maisel.showcase.composables.SignInPage
import com.maisel.signup.SignUpActivity
import com.maisel.state.AuthResultState
import com.maisel.ui.MainTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
class SignInActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: SignInViewModel by lazy {
        ViewModelProvider(this)[SignInViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInCurrentUser()

        setContent {
            MainTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SignInPage(
                        viewModel,
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
        lifecycleScope.launchWhenStarted {
            viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collectLatest {
                render(it)
            }
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
                Log.d("joshua", "activity success")
                DashboardActivity.createIntent(this).also { startActivity(it) }
                finish()
            }
            AuthResultState.Error -> {
                Log.d("joshua", "activity error")
            }
        }
    }

    /**
     *  TODO: Create splash screen
     *  Move this method to splash screen
     */
    private fun signInCurrentUser() {
        if (viewModel.isUserLoggedIn()) {
            DashboardActivity.createIntent(this).also { startActivity(it) }
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
        viewModel.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
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

    companion object {
        private const val RC_SIGN_IN = 65

        fun createIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}
