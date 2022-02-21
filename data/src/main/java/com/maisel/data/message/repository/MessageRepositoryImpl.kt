package com.maisel.data.message.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maisel.data.message.model.MessageData
import com.maisel.data.message.toMessageData
import com.maisel.data.message.toMessageModel
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.user.entity.SignUpUser
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

    override fun startListeningToMessages(senderRoom: String) {
        if (messageListeners != null) {
            Log.w("MessageRepositoryImpl", " Calling start listening while already started")
            return
        }
        messageListeners =
            database.ref.child("chats")
                .child(senderRoom).addValueEventListener(object : ValueEventListener {
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
        senderRoom: String,
        receiverRoom: String,
        receiverId: String,
        model: MessageModel
    ) {
//        if (sendMessageSenderListeners != null && sendMessageReceiverListeners != null) {
//            Log.w("Message Repo send:", " Calling start listening while already started")
//            return
//        }
        sendMessageSenderListeners = database.ref.child("chats")
            .child(senderRoom)
            .push()
            .setValue(model.toMessageData())
            .addOnSuccessListener {
                sendMessageReceiverListeners = database.ref.child("chats")
                    .child(receiverRoom)
                    .push()
                    .setValue(model.toMessageData())
                    .addOnSuccessListener {

                    }
            }

        updateLastMessage(receiverId, model)
    }

    //TODO: Convert to coroutine
    private fun updateLastMessage(receiverId: String, model: MessageModel) {
        database.child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        val user = child.getValue(SignUpUser::class.java)
                        user?.userId?.let {
                            if (receiverId == it) {
                                    database.child("Users").child(receiverId) //TODO: Remove listener
                                        .setValue(user.copy(lastMessage = model.message))
                                        .addOnSuccessListener {

                                        }
                                        .addOnFailureListener {

                                        }
                                        .addOnCompleteListener {

                                        }

                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun stopListeningToMessages(senderRoom: String) {
        messageListeners?.let {
            database.ref.child("chats").child(senderRoom).removeEventListener(it)
        }
        messageListeners = null
        listOfMessages.onNext(emptyList())
    }

    override fun stopListeningToSendMessages(senderRoom: String) {
//        sendMessageReceiverListeners = null
//        sendMessageSenderListeners = null
    }

    override fun startListeningToLastMessages(userId: String) {
        database.ref.child("chats")
            .child(firebaseAuth.uid + userId)
            .orderByChild("timestamp")
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        lastMessageListeners.onNext(
                            snapshot.children.firstOrNull()?.child("message")?.value.toString()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

//    suspend fun startListeningToLastMessages2(userId: String) : String {
//        withContext(DispatcherProvider.IO) {
//            try {
//                database.ref.child("chats")
//                    .child(firebaseAuth.uid + userId)
//                    .orderByChild("timestamp")
//                    .limitToLast(1)
//                    .addListenerForSingleValueEvent( object: ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.hasChildren()) {
//                                lastMessageListeners.onNext(snapshot.children.firstOrNull()?.child("message")?.value.toString())
//                            }
//                            return ""
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
//                        }
//
//                    })
//            } catch (e: Exception) {
//                Log.d("joshua exception", e.toString())
//                null
//            }
//        }
//    }

    // https://medium.com/swlh/how-to-use-firebase-realtime-database-with-kotlin-coroutine-flow-946fe4cf2cd9
    override fun fetchLastMessage(userId: String) = callbackFlow<Result<String>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                this@callbackFlow.sendBlocking(
                    Result.success(
                        dataSnapshot.children.firstOrNull()?.child("message")?.value.toString()
                    )
                )
            }
        }

        database.ref.child("chats")
            .child(firebaseAuth.uid + userId)
            .orderByChild("timestamp")
            .limitToLast(1)
            .addListenerForSingleValueEvent(postListener)

        awaitClose {
            database.ref.child("chats")
                .child(firebaseAuth.uid + userId)
                .orderByChild("timestamp")
                .limitToLast(1)
                .removeEventListener(postListener)
        }
    }

    override fun observeLastMessage(): Observable<String> = lastMessageListeners

    companion object {
        const val LAST_MESSAGE_REFERENCE = "last_message"
    }
}
