package com.maisel.data.message.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maisel.data.coroutine.DispatcherProvider
import com.maisel.data.firebase.observeLastMessage
import com.maisel.data.message.dao.MessageDao
import com.maisel.data.message.dao.RecentMessageDao
import com.maisel.data.message.mapper.*
import com.maisel.data.message.model.MessageData
import com.maisel.domain.message.ChatModel
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class MessageRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val database: DatabaseReference,
    private val messageDao: MessageDao,
    private val recentMessageDao: RecentMessageDao
) : MessageRepository {

    private var sendMessageReceiverListeners: Task<Void>? = null
    private var sendMessageSenderListeners: Task<Void>? = null

    override fun listenToChatMessages(
        senderId: String,
        receiverId: String
    ): Flow<Result<List<ChatModel>>> {
        return callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.sendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ChatModel>()
                    snapshot.children.forEach { children ->
                        val messageData = children.getValue(MessageData::class.java)
                        messageData?.toChatModel()?.let(list::add)
                    }

                    this@callbackFlow.sendBlocking(Result.success(list))
                }
            }

            database.ref.child(MESSAGES)
                .child(senderId)
                .child(receiverId)
                .addValueEventListener(postListener)

            awaitClose {
                database.ref.child(MESSAGES)
                    .child(senderId)
                    .child(receiverId)
                    .removeEventListener(postListener)
            }
        }
    }

    override fun getSenderUid(): String? {
        return firebaseAuth.uid
    }

    override fun sendMessage(
        input: String,
        senderUid: String,
        receiverId: String,
        model: ChatModel
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

    private fun setLatestMessage(receiverId: String, model: ChatModel) {
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

    override fun listenToRecentMessages() = callbackFlow<Result<List<MessageModel>>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MessageModel>()
                snapshot.children.forEach { children ->
                    val latestMessages =
                        children.getValue(MessageData::class.java)?.toMessageModel(children.key)
                            ?: MessageData().toMessageModel(children.key)

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

    override suspend fun insertRecentMessages(messages: List<MessageModel>) {
        recentMessageDao.insertRecentMessages(messages.map { it.toRecentMessageEntity() })
    }

    override suspend fun getRecentMessages(): Flow<List<ChatModel>> {
        return withContext(DispatcherProvider.IO) {
            recentMessageDao.getRecentMessages()
                .distinctUntilChanged()
                .map { listOfRecentMessagesEntity ->
                    listOfRecentMessagesEntity.map { entity ->
                        entity.toChatModel()
                    }
                }
        }
    }

    override suspend fun insertMessages(messages: List<ChatModel>) {
        messageDao.insertMessages(messages.map { it.toMessageEntity() })
    }

    override suspend fun getListOfChatMessages(): Flow<List<ChatModel>> {
        return withContext(DispatcherProvider.IO) {
            messageDao.getMessages()
                .distinctUntilChanged()
                .map { listOfMessagesEntity ->
                    listOfMessagesEntity.map { entity ->
                        entity.toChatModel()
                    }
                }
        }
    }

    companion object {
        const val MESSAGES = "messages"
        const val LATEST_MESSAGES = "latest-messages"
    }
}
