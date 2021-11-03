package com.maisel.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.maisel.R
import com.maisel.common.BaseFragmentActivity
import com.maisel.onboarding.composables.OnboardingCarousel
import com.maisel.databinding.ActivityMainBinding
import com.maisel.signin.SignInActivity
import com.maisel.ui.OnBoardingTheme
import kotlinx.android.synthetic.main.activity_main.view.*

@ExperimentalPagerApi
@ExperimentalAnimationApi
class MainActivity : BaseFragmentActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

   private lateinit var binding: ActivityMainBinding
   private lateinit var viewPager2: ViewPager2

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

                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    OnboardingCarousel()
                }
            }
        }
        //  window.statusBarColor = Color.WHITE
//        setContent {
//            MaterialTheme() {
//                Surface(color = MaterialTheme.colors.background) {
//                    LoginPage()
//                }
//            }
//        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//       //setSupportActionBar(binding.root.toolbar)
//        binding.root.toolbar?.overflowIcon?.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//        render()
//    }
        render()
    }

    private fun render() {
        createViewPager()
        setTabLayout()
    }

    private fun createViewPager() {
        viewPager2 = binding.root.viewPager2
        viewPager2.adapter = FragmentsAdapter(this)
    }

    private fun setTabLayout() {
        val tabLayout = binding.root.tabLayout
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position + 1) {
                1 -> "Chats"
                2 -> "Status"
                3 -> "Calls"
                else -> throw Exception("Wrong number of tabs")
            }

        }.attach()
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