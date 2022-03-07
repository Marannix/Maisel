package com.maisel.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.R
import com.maisel.chat.ChatDetailActivity
import com.maisel.common.BaseFragmentActivity
import com.maisel.dashboard.chat.ChatsFragment
import com.maisel.databinding.ActivityMainBinding
import com.maisel.domain.user.entity.User

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
class DashboardActivity : BaseFragmentActivity(), ChatsFragment.ChatsFragmentCallback,
    DashboardFragment.DashboardFragmentCallback {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this)[DashboardViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    private fun setUp() {
        replaceFragment(DashboardFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOpenChatsDetails(user: User) {
        ChatDetailActivity.createIntent(this, user).also {
            startActivity(it)
        }
    }

    override fun openContactsList() {
        replaceFragment(ChatsFragment())
    }

    //TODO: Replace with Jetpack Navigation
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.dashboardFragmentContainer.id, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }
}
