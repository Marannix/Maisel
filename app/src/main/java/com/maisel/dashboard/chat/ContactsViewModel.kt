package com.maisel.dashboard.chat

import com.maisel.common.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.dashboard.DashboardViewState
import com.maisel.domain.user.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    userComposerController: UserComposerController
) : BaseViewModel() {

    val users: StateFlow<List<User>> = userComposerController.users

}
