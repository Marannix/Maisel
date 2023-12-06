package com.maisel.chatdetail

import androidx.compose.runtime.Immutable
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.User

interface ChatDetailsContract {

    abstract class ViewModel : UpdatedBaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val isLoading: Boolean,
        val recipient: User?,
        val senderUid: String?,
        val error: String?,
        val messages: List<MessageItem>
    ) : UiStateBase {

    }

    sealed class UiEvents : UiEventBase {
        object BackPressed : UiEvents()
        object VideoClicked : UiEvents()
        object CallClicked : UiEvents()
    }
}
