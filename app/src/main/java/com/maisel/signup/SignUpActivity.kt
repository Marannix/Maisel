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
import com.maisel.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get<SignUpViewModel>(
            SignUpViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.valueOf("DEBUG"));
        observeAuthState()
        binding.signUpButton.setOnClickListener {
            //TODO: Make sure nothing is null or empty
            //TODO: Hide password by default
            //TODO: Add regex
            viewModel.registerUser(
                binding.editTextName.text.toString(),
                binding.editTextEmailAddress.text.toString(),
                binding.editTextPassword.text.toString()
            )
            //.observeOn(AndroidSchedulers.mainThread())
//                .subscribe { result ->
//                when (result) {
//                    AuthResultState.Error -> {
//                        Log.d("joshua", "authresultstate error")
//                    }
//                    AuthResultState.Loading -> {
//                        Log.d("joshua", "authresultstate loading")
//                    }
//                    AuthResultState.Success -> {
//                        Log.d("joshua", "authresultstate success")
//                    }
//                    AuthResultState.Idle -> {
//                        Log.d("joshua", "authresultstate idle")
//                    }
//                }
//            }
        }
    }

    fun observeAuthState() {
        viewModel.viewState.observe(this, { state ->
            when (state) {
                AuthResultState.Error -> {
                    Log.d("joshua", "activity error")
                }
                AuthResultState.Loading -> {
                    Log.d("joshua", "activity loading")
                }
                AuthResultState.Success -> {
                    Log.d("joshua", "activity success")
                }
                AuthResultState.Idle -> {
                    Log.d("joshua", "activity idle")
                }
            }
        })
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}