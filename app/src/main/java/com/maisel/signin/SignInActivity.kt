package com.maisel.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maisel.R
import com.maisel.databinding.ActivitySignInBinding
import com.maisel.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.signUpButton.setOnClickListener {
            SignUpActivity.createIntent(this).also { startActivity(it) }
        }
    }
}