package com.maisel.domain.message

import io.reactivex.Observable

interface MessageRepository {

    fun startListeningToMessages(senderRoom: String)

    fun stopListeningToMessages(senderRoom: String)

    fun observeListOfMessages(): Observable<List<MessageModel>>

    fun getSenderUid(): String?
}
