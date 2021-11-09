package com.maisel.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.maisel.databinding.ActivitySignInBinding
import com.maisel.signup.OLDSignUpActivity
import com.maisel.state.AuthResultState

@ExperimentalAnimationApi
@ExperimentalPagerApi
class OLDSignInActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, OLDSignInActivity::class.java)
        }
    }

    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 65

    private val viewModel: SignInViewModel by lazy {
        ViewModelProvider(this).get(
            SignInViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //supportActionBar?.hide()

        signInCurrentUser()

        observeViewState()

        binding.signUpButton.setOnClickListener {
            OLDSignUpActivity.createIntent(this).also { startActivity(it) }
        }

        binding.signInButton.setOnClickListener {
            validateSignIn()
        }

        binding.googleLoginButton.setOnClickListener {
            signInWithGoogle()
        }

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
                binding.signInButton.setLoading()
                Log.d("joshua", "activity loading")
            }
            is AuthResultState.Success -> {
                viewModel.setUser(state.authResultState.user)
                binding.signInButton.setComplete()
                Log.d("joshua", "activity success")
                MainActivity.createIntent(this).also { startActivity(it) }
                finish()
            }
            AuthResultState.Error -> {
                binding.signInButton.setFailed()
                Log.d("joshua", "activity error")
            }
        }

        state.signInValidator.showEmailError.let { showEmailError ->
            if (showEmailError) {
                binding.editTextInputEmailLayout.error = "Please enter a valid email"
            }
            binding.editTextInputEmailLayout.isErrorEnabled = showEmailError
        }
    }

    private fun validateSignIn() {
        if (viewModel.isEmailAddressValid(binding.editTextEmailAddress.text.toString())) {
            signInUser()
        }
    }

    private fun signInUser() {
        viewModel.signInWithEmailAndPassword(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text.toString())
    }

    private fun createGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        viewModel.signInWithCredential(idToken, GoogleAuthProvider.getCredential(idToken, null))
    }
}