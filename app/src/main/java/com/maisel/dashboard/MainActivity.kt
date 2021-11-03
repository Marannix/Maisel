package com.maisel.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.R
import com.maisel.common.BaseActivity
import com.maisel.dashboard.composables.LoginPage
import com.maisel.signin.SignInActivity
import com.maisel.ui.OnBoardingTheme

@ExperimentalPagerApi
@ExperimentalAnimationApi
class MainActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

//    private lateinit var binding: ActivityMainBinding

    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(
            DashboardViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OnBoardingTheme {
                window.statusBarColor = MaterialTheme.colors.background.toArgb()
                window.navigationBarColor = MaterialTheme.colors.background.toArgb()

                Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                    LoginPage()
                }
            }
        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setSupportActionBar(binding.root.toolbar)
//        binding.root.toolbar?.overflowIcon?.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {

            }
            R.id.logout -> {
                viewModel.logOutUser()
                SignInActivity.createIntent(this).also { startActivity(it) }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}