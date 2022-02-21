package com.maisel.message

import com.maisel.domain.message.MessageModel

data class MessageState(val input: String = "",
                        val senderUid: String? = null,
                        val senderRoom: String? = null,
                        val receiverRoom: String? = null,
                        val receiverId: String? = null,
                        val model: MessageModel? = null)
