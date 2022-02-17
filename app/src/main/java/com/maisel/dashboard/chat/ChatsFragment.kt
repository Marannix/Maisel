package com.maisel.dashboard.chat

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maisel.dashboard.DashboardViewModel
import com.maisel.dashboard.chat.composables.ChatsList
import com.maisel.domain.user.entity.SignUpUser
import com.maisel.domain.user.usecase.GetUsersUseCase
import com.maisel.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private var callback: ChatsFragmentCallback? = null

    interface ChatsFragmentCallback {
        fun onOpenChatsDetails(user: SignUpUser)
    }

    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(
            DashboardViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewState()
        viewModel.getUsers()
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
                val users =
                    viewModel.viewState.observeAsState().value?.users ?: emptyList()
                MainTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        ChatsList(users, callback, viewModel)
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
                Toast.makeText(activity, "Chats Fragment Error", Toast.LENGTH_SHORT).show()
            }
            GetUsersUseCase.UserDataState.Loading -> {
                Toast.makeText(activity, "Chats Fragment Loading", Toast.LENGTH_SHORT).show()
            }
            is GetUsersUseCase.UserDataState.Success -> {
                Toast.makeText(activity, "Chats Fragment Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
