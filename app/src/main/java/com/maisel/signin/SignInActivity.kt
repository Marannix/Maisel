package com.maisel.signin

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.maisel.MainActivity
import com.maisel.common.BaseActivity
import com.maisel.databinding.ActivitySignInBinding
import com.maisel.signup.SignUpActivity
import com.maisel.state.AuthResultState

class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val viewModel: SignInViewModel by lazy {
        ViewModelProvider(this).get(
            SignInViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        observeViewState()

        binding.signUpButton.setOnClickListener {
            SignUpActivity.createIntent(this).also { startActivity(it) }
        }

        binding.signInButton.setOnClickListener {
            validateSignIn()
        }
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: SignInViewState) {
        when (state.authResultState) {
            AuthResultState.Error -> {
                binding.signInButton.setFailed()
                Log.d("joshua", "activity error")
            }
            AuthResultState.Loading -> {
                binding.signInButton.setLoading()
                Log.d("joshua", "activity loading")
            }
            AuthResultState.Success -> {
                binding.signInButton.setComplete()
                Log.d("joshua", "activity success")
                MainActivity.createIntent(this).also { startActivity(it) }
                finish()
            }
            AuthResultState.Idle -> {
                Log.d("joshua", "activity idle")
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
}