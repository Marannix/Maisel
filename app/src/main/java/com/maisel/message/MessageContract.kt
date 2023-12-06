package com.maisel.message

import androidx.compose.runtime.Immutable
import com.maisel.chatdetail.MessageItem
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel
import com.maisel.domain.message.ChatModel
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.User
import com.maisel.signin.SignInContract

interface MessageContract {

    abstract class ViewModel : UpdatedBaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val input: String,
        val senderUid: String?,
        val senderRoom: String?,
        val receiverRoom: String?,
        val receiverId: String?,
        val model: ChatModel?
    ) : UiStateBase {

    }

    sealed class UiEvents : UiEventBase {
        data class SendMessage(val message: String) : UiEvents()
        data class MessageUpdated(val message: String) : UiEvents()
        object AttachmentsClicked : UiEvents()
        object CameraClicked : UiEvents()

    }
}
