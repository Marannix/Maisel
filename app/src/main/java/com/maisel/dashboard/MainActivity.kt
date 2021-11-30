package com.maisel.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.tabs.TabLayoutMediator
import com.maisel.R
import com.maisel.common.BaseFragmentActivity
import com.maisel.databinding.ActivityMainBinding
import com.maisel.signin.OLDSignInActivity
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.toolbar.inflateMenu(R.menu.menu)
        binding.root.toolbar.title = "Maisel"
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
                OLDSignInActivity.createIntent(this).also { startActivity(it) }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}