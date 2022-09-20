package com.maisel.dashboard.chat

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maisel.domain.user.entity.User
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
@Deprecated("TODO: This activity can be deleted")
class ContactsFragment : Fragment() {

    private var callback: ContactsFragmentCallback? = null

    interface ContactsFragmentCallback {
        fun onOpenChatsDetails(receiverUser: User, path: String)
        fun onContactsBackPressed()
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

        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
