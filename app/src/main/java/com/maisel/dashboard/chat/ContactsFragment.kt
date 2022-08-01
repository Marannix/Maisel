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
import com.google.accompanist.insets.ProvideWindowInsets
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.DashboardViewModel
import com.maisel.dashboard.chat.composables.ContactList
import com.maisel.domain.user.entity.User
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var callback: ContactsFragmentCallback? = null

    interface ContactsFragmentCallback {
        fun onOpenChatsDetails(receiverUser: User, path: String)
    }

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this)[ContactsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = try {
            activity as ContactsFragmentCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity must implement ContactsFragment "
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
                    ProvideWindowInsets(
                        windowInsetsAnimationsEnabled = true,
                        consumeWindowInsets = false
                    ) {
                        Surface(color = ChatTheme.colors.appBackground) {
                            ContactList(viewModel, callback)
                        }
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
