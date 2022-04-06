package com.maisel.dashboard

import com.maisel.domain.message.MessageModel
import com.maisel.state.UserAuthState

data class DashboardViewState(val userAuthState: UserAuthState? = UserAuthState.EMPTY, val recentMessageState: RecentMessageState = RecentMessageState.Loading )

sealed class RecentMessageState {
    object Loading : RecentMessageState()
    data class Success(val listOfMessages: List<MessageModel>) : RecentMessageState()
    data class Error(val message: String) : RecentMessageState()
}
