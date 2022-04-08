package com.maisel.dashboard.chat

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardViewModel
import com.maisel.dashboard.chat.composables.ContactList
import com.maisel.domain.user.entity.User
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
//Rename ContactsFragment
class ChatsFragment : Fragment() {

    private var callback: ChatsFragmentCallback? = null

    interface ChatsFragmentCallback {
        fun onOpenChatsDetails(user: User)
    }

    //TODO: ContactsViewModel
    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(
            DashboardViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = try {
            activity as ChatsFragmentCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity must implement ChatsFragmentCallback "
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatTheme {
                    Surface(color = ChatTheme.colors.appBackground) {
                        ContactList(viewModel, callback)
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
