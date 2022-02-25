package com.maisel.dashboard

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maisel.compose.ui.components.dashboard.DashboardScreen
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.dashboard.chat.DashboardViewState
import com.maisel.domain.user.usecase.GetUsersUseCase
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var callback: DashboardFragmentCallback? = null

    interface DashboardFragmentCallback {
        fun openContactsList()
    }

    private val viewModel: DashboardViewModel by lazy { ViewModelProvider(this)[DashboardViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewState()
        viewModel.getUsers()
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

    private fun observeViewState() {
        viewModel.viewState.observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: DashboardViewState) {
        when (state.use) {
            GetUsersUseCase.UserDataState.Error -> {
                Toast.makeText(activity, "Dashboard Error", Toast.LENGTH_SHORT).show()
            }
            GetUsersUseCase.UserDataState.Loading -> {
                Toast.makeText(activity, "Dashboard Loading", Toast.LENGTH_SHORT).show()
            }
            is GetUsersUseCase.UserDataState.Success -> {
                Toast.makeText(activity, "Dashboard Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
