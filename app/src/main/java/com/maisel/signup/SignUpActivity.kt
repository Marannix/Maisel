package com.maisel.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.maisel.common.BaseActivity
import com.maisel.databinding.ActivitySignUpBinding
import com.maisel.state.AuthResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(
            SignUpViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewState()
        binding.signUpButton.setOnClickListener {
            validateSignUp()
        }
    }

    private fun validateSignUp() {
        if (viewModel.isNameValid(binding.editTextName.text.toString())
            && viewModel.isEmailAddressValid(binding.editTextEmailAddress.text.toString())
            && viewModel.isPasswordValid(binding.editTextPassword.text.toString())
        ) {
            registerUser()
        }
    }

    private fun registerUser() {
        viewModel.registerUser(
            binding.editTextName.text.toString(),
            binding.editTextEmailAddress.text.toString(),
            binding.editTextPassword.text.toString()
        )
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: SignUpViewState) {
        when (state.authResultState) {
            AuthResultState.Error -> {
                binding.signUpButton.setFailed()
            }
            AuthResultState.Loading -> {
                binding.signUpButton.setLoading()
            }
            is AuthResultState.Success -> {
                binding.signUpButton.setComplete()
            }
            AuthResultState.Idle -> {
                Log.d("joshua", "activity idle")
            }
        }

        state.signUpValidator.showPasswordError.let { showPasswordError ->
            if (showPasswordError) {
                binding.editTextInputPasswordLayout.error = "Password must be 8 characters long"
            }
            binding.editTextInputPasswordLayout.isErrorEnabled = showPasswordError
        }

        state.signUpValidator.showNameError.let { showNameError ->
            if (showNameError) {
                binding.editTextInputNameLayout.error = "Please enter a valid name"
            }
            binding.editTextInputNameLayout.isErrorEnabled = showNameError
        }

        state.signUpValidator.showEmailError.let { showEmailError ->
            if (showEmailError) {
                binding.editTextInputEmailLayout.error = "Please enter a valid email"
            }
            binding.editTextInputEmailLayout.isErrorEnabled = showEmailError
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}