package com.maisel.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.maisel.R
import com.maisel.databinding.ActivitySignInBinding
import com.maisel.databinding.ActivitySignUpBinding
import com.maisel.repository.UserRepository

class SignUpActivity : AppCompatActivity() {

    private val userRepository = UserRepository()
    private lateinit var binding: ActivitySignUpBinding

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.valueOf("DEBUG"));

        binding.signUpButton.setOnClickListener {
            //TODO: Make sure nothing is null or empty
            //TODO: Hide password by default
            //TODO: Add regex
            userRepository.createAccount(
                binding.editTextName.text.toString(),
                binding.editTextEmailAddress.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }
    }
}