package com.maisel.dashboard

import com.maisel.common.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.domain.user.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userComposerController: UserComposerController
) : BaseViewModel() {

    val currentUser: StateFlow<User> = userComposerController.currentUser

    val users: StateFlow<List<User>> = userComposerController.users

    val viewState: StateFlow<DashboardViewState> = userComposerController.state

    init {
        userComposerController.setLoggedInUser()
        userComposerController.getLoggedInUser()
        userComposerController.listOfUsers()
        userComposerController.getUsers()
        userComposerController.listenToRecentMessages()
        userComposerController.getRecentMessages()
    }

    fun logOutUser() {
        userComposerController.logoutUser()
    }

    fun getMenuItems() : List<DashboardDrawerMenuItem> {
        return emptyList()
    }
    /**
     * Disposes the inner [DashboardViewModel].
     */
    override fun onCleared() {
        super.onCleared()
        userComposerController.onCleared()
    }
}
