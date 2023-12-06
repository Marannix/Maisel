package com.maisel.chatdetail

import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.User

data class ChatDetailViewState(
    val recipient: User? = null,
    val senderUid: String? = null
) {

}
