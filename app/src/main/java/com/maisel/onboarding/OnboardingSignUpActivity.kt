package com.maisel.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.maisel.common.BaseActivity
import com.maisel.onboarding.composables.SignUpPage
import com.maisel.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class OnboardingSignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SignUpPage()
                }
            }
        }

    }
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, OnboardingSignUpActivity::class.java)
        }
    }
}