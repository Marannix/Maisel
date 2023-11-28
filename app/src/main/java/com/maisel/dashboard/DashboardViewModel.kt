package com.maisel.dashboard

import com.maisel.common.base.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userComposerController: UserComposerController,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val currentUser: StateFlow<User> = userComposerController.currentUser

    val users: StateFlow<List<User>> = userComposerController.users

    val viewState: StateFlow<DashboardViewState> = userComposerController.state

    val isLoggedIn = userRepository.isUserLoggedIn

    init {
        userComposerController.setLoggedInUser()
        userComposerController.listOfUsers()
        userComposerController.getUsers()
        userComposerController.listenToRecentMessages()
        userComposerController.getRecentMessages()
    }

    fun logOutUser() {
        userComposerController.logoutUser()
    }

    fun getMenuItems(): List<DashboardDrawerMenuItem> {
        return DashboardDrawerMenuItem.get()
    }

    /**
     * Disposes the inner [DashboardViewModel].
     */
    override fun onCleared() {
        super.onCleared()
        userComposerController.onCleared()
    }
}
