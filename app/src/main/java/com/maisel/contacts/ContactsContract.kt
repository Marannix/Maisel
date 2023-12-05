package com.maisel.contacts

import androidx.compose.runtime.Immutable
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel
import com.maisel.domain.user.entity.User

interface ContactsContract {

    abstract class ViewModel : UpdatedBaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val isLoading: Boolean,
        val contacts: List<User>,
    ) : UiStateBase

    sealed class UiEvents : UiEventBase {
        data class OnContactClicked(val contactId: String?) : UiEvents()
    }
}
