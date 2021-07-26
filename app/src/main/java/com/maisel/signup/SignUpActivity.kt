package com.maisel.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
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

        binding.signUpButton.setOnClickListener {
            userRepository.createAccount(binding.editTextEmailAddress.text.toString(), binding.editTextPassword.text. toString())
        }
    }
}