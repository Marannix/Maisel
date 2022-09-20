package com.maisel.state

import com.maisel.chatdetail.MessageItem

sealed class MessageItemState {

    object Loading : MessageItemState()
    data class Success(val messages: MessageItem): MessageItemState()
    object Error : MessageItemState()
    object Idle : MessageItemState()
}
