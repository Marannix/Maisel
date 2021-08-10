package com.maisel.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.maisel.databinding.ActivitySignUpBinding
import com.maisel.state.AuthResultState
import com.maisel.state.SignUpViewState
import com.maisel.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

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
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.valueOf("DEBUG"));
        observeViewState()
        binding.signUpButton.setOnClickListener {
            //TODO: Make sure nothing is null or empty
            //TODO: Hide password by default
            //TODO: Add regex
            viewModel.registerUser(
                binding.editTextName.text.toString(),
                binding.editTextEmailAddress.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }
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
                Log.d("joshua", "activity error")
            }
            AuthResultState.Loading -> {
                binding.signUpButton.setLoading()
                Log.d("joshua", "activity loading")
            }
            AuthResultState.Success -> {
                binding.signUpButton.setComplete()
                Log.d("joshua", "activity success")
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