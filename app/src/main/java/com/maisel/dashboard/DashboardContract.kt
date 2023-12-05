package com.maisel.dashboard

import androidx.compose.runtime.Immutable
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel
import com.maisel.domain.user.entity.User
import com.maisel.state.UserAuthState

interface DashboardContract {

    abstract class ViewModel : UpdatedBaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val isLoading: Boolean,
        val currentUser: User?,
        val contacts: List<User>,
        val recentMessageState: RecentMessageState,
        val userAuthState: UserAuthState,
    ) : UiStateBase

    sealed class UiEvents : UiEventBase {
        data class EmailUpdated(val email: String) : UiEvents()
        data class RecentMessageClicked(val receiverUserId: String?) : UiEvents()
        object LogoutClicked : UiEvents()
    }
}
