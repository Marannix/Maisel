package com.maisel.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.tabs.TabLayoutMediator
import com.maisel.R
import com.maisel.chat.ChatDetailActivity
import com.maisel.common.BaseFragmentActivity
import com.maisel.dashboard.chat.ChatsFragment
import com.maisel.databinding.ActivityMainBinding
import com.maisel.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_main.view.*

@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
class DashboardActivity : BaseFragmentActivity(), ChatsFragment.ChatsFragmentCallback {

   private lateinit var binding: ActivityMainBinding
   private lateinit var viewPager2: ViewPager2

    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this)[DashboardViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.toolbar.inflateMenu(R.menu.menu)
        binding.root.toolbar.title = "Maisel"
        binding.root.toolbar.setOnMenuItemClickListener(toolbarListener)
        setUp()
    }

    private fun setUp() {
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

//    private fun observeViewState() {
//        viewModel.viewState.observe(this) { state ->
//            render(state)
//        }
//    }
//
//    private fun render(state: DashboardViewState) {
//        state.getSelectedUser?.let {
//            openChatsDetailScreen()
//            viewModel.selectedUser(null)
//        }
//    }

    /**
     * For some odd reason onCreateOptionsMenu and onOptionsItemSelected are not being called
     * I manually attach the listeners to toolbar as a temp solution
     */
    private val toolbarListener =
        Toolbar.OnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.settings -> {
                    notImplementedYet()
                }
                R.id.logout -> {
                    viewModel.logOutUser()
                    SignInActivity.createIntent(this@DashboardActivity).also { startActivity(it) }
                    finish()
                }
            }
            false
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startListeningToUser()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopListeningToUser()
    }

    override fun onDestroy() {
        binding.root.toolbar.setOnMenuItemClickListener(null)
        super.onDestroy()
    }

    override fun onOpenChatsDetails() {
        ChatDetailActivity.createIntent(this).also { startActivity(it) }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }
}
