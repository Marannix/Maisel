package com.maisel.data.message.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maisel.data.firebase.observeLastMessage
import com.maisel.data.message.model.MessageData
import com.maisel.data.message.toMessageData
import com.maisel.data.message.toMessageModel
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class MessageRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val database: DatabaseReference
) : MessageRepository {

    private var listOfMessages = BehaviorSubject.create<List<MessageModel>>()
    private var messageListeners: ValueEventListener? = null
    private var sendMessageReceiverListeners: Task<Void>? = null
    private var sendMessageSenderListeners: Task<Void>? = null

    private var lastMessageListeners = BehaviorSubject.create<String>()

    override fun startListeningToMessages(senderId: String, receiverId: String) {
        if (messageListeners != null) {
            Log.w("MessageRepositoryImpl", " Calling start listening while already started")
            return
        }
        messageListeners =
            database.ref.child(MESSAGES)
                .child(senderId)
                .child(receiverId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<MessageModel>()
                        snapshot.children.forEach { children ->
                            val messageData = children.getValue(MessageData::class.java)
                            messageData?.toMessageModel()?.let(list::add)
                        }
                        listOfMessages.onNext(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // TODO
                    }
                })
    }

    //TODO: Store in room database
    override fun observeListOfMessages(): Observable<List<MessageModel>> = listOfMessages

    override fun getSenderUid(): String? {
        return firebaseAuth.uid
    }

    override fun sendMessage(
        input: String,
        senderUid: String,
        receiverId: String,
        model: MessageModel
    ) {
//        if (sendMessageSenderListeners != null && sendMessageReceiverListeners != null) {
//            Log.w("Message Repo send:", " Calling start listening while already started")
//            return
//        }

        sendMessageSenderListeners = database.ref.child(MESSAGES)
            .child(senderUid)
            .child(receiverId)
            .push()
            .setValue(model.toMessageData())
            .addOnSuccessListener {
                sendMessageReceiverListeners = database.ref.child(MESSAGES)
                    .child(receiverId)
                    .child(senderUid)
                    .push()
                    .setValue(model.toMessageData())
                    .addOnSuccessListener {

                    }
            }

        setLatestMessage(receiverId, model)
    }

    override fun stopListeningToMessages(senderId: String, receiverId: String) {
        messageListeners?.let {
            database.ref.child(MESSAGES)
                .child(senderId)
                .child(receiverId)
                .removeEventListener(it)
        }
        messageListeners = null
        listOfMessages.onNext(emptyList())
    }

    override fun stopListeningToSendMessages(senderRoom: String) {
//        sendMessageReceiverListeners = null
//        sendMessageSenderListeners = null
    }

    // https://medium.com/swlh/how-to-use-firebase-realtime-database-with-kotlin-coroutine-flow-946fe4cf2cd9
    override fun fetchLastMessage(userId: String) = callbackFlow<Result<String>> {
        database.ref.child(LATEST_MESSAGES)
            .child(userId)
            .observeLastMessage()
    }

    private fun setLatestMessage(receiverId: String, model: MessageModel) {
        firebaseAuth.uid?.let { firebaseAuthUid ->
            database.ref.child(LATEST_MESSAGES)
                .child(firebaseAuthUid)
                .child(receiverId)
                .setValue(model.toMessageData())
                .addOnSuccessListener {
                    database.ref.child(LATEST_MESSAGES)
                        .child(receiverId)
                        .child(firebaseAuthUid)
                        .setValue(model.toMessageData())
                        .addOnSuccessListener {

                        }
                }
        }
    }

    override fun getLatestMessagev2() = callbackFlow<Result<List<MessageModel>>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MessageModel>()
                snapshot.children.forEach { children ->
                    val latestMessages =
                        children.getValue(MessageData::class.java)?.toMessageModel()
                            ?: MessageData().toMessageModel()

                    list.add(latestMessages)
                }
                this@callbackFlow.sendBlocking(Result.success(list))
            }
        }

        firebaseAuth.uid?.let { firebaseAuthUid ->
            database.ref.child(LATEST_MESSAGES)
                .child(firebaseAuthUid)
                .addValueEventListener(postListener)


            awaitClose {
                database.ref.child(LATEST_MESSAGES)
                    .child(firebaseAuthUid)
                    .removeEventListener(postListener)
            }
        }
    }

    override fun observeLastMessage(): Observable<String> = lastMessageListeners

    companion object {
        const val MESSAGES = "messages"
        const val LATEST_MESSAGES = "latest-messages"
    }
}
