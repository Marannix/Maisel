package com.maisel.dashboard

import com.maisel.common.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.domain.message.MessageModel
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val userComposerController: UserComposerController
) : BaseViewModel() {

    val currentUser: StateFlow<User> = userComposerController.currentUser

    val users: StateFlow<List<User>> = userComposerController.users

    val latestMessages: StateFlow<List<MessageModel>> = userComposerController.latestMessages

    init {
        userComposerController.setLoggedInUser()
        userComposerController.getLoggedInUser()
        userComposerController.listOfUsers()
        userComposerController.getUsers()
        userComposerController.getLatestMessages()
    }

    fun logOutUser() {
        logOutUseCase.invoke()
    }

    /**
     * Disposes the inner [DashboardViewModel].
     */
    override fun onCleared() {
        super.onCleared()
        userComposerController.onCleared()
    }
}
