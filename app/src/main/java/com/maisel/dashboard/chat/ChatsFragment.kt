package com.maisel.dashboard.chat

import android.os.Bundle
import android.util.Log
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
import com.maisel.domain.user.usecase.GetUsersUseCase
import com.maisel.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class ChatsFragment : Fragment() {

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
                        ChatsList(users)
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
                Log.d("joshua123", state.use.toString())
            }
        }
    }
}
