package com.maisel.dashboard

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maisel.compose.ui.components.dashboard.DashboardScreen
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.domain.user.entity.User
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var callback: DashboardFragmentCallback? = null

    interface DashboardFragmentCallback {
        fun onOpenChatsDetails(user: User)
        fun openContactsList()
        fun onLogOut()
    }

    private val viewModel: DashboardViewModel by lazy { ViewModelProvider(requireActivity())[DashboardViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = try {
            activity as DashboardFragmentCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity must implement DashboardFragmentCallback "
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
                    Surface(color = MaterialTheme.colors.background) {
                        DashboardScreen(viewModel, callback)
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
