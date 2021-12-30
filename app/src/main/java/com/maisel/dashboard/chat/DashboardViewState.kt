package com.maisel.dashboard.chat

import com.maisel.domain.user.entity.SignUpUser
import com.maisel.domain.user.usecase.GetUsersUseCase

data class DashboardViewState(val use: GetUsersUseCase.UserDataState? = null, val selectedUser: SignUpUser? = null) {
    val users = if (use is GetUsersUseCase.UserDataState.Success) {
        use.user
    } else emptyList()

    val getSelectedUser = selectedUser
}
