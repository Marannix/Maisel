package com.maisel.dashboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.maisel.dashboard.calls.CallsFragment
import com.maisel.dashboard.chat.ChatsFragment
import com.maisel.dashboard.status.StatusFragment

@ExperimentalComposeUiApi
class FragmentsAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position + 1) {
            1 -> ChatsFragment()
            2 -> StatusFragment()
            3 -> CallsFragment()
            else -> throw Exception("This fragment does not exist")//TODO: Throw Exception
        }
    }

}
