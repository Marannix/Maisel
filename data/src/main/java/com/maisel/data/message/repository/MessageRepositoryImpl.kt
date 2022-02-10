package com.maisel.data.message.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class MessageRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val database: DatabaseReference
) : MessageRepository {

    private var listOfMessages = BehaviorSubject.create<List<MessageModel>>()
    private var messageListeners: ValueEventListener? = null
    private var sendMessageListeners: ValueEventListener? = null

    override fun startListeningToMessages(senderRoom: String) {
//        if (messageListeners != null) {
//            Log.w("MessageRepositoryImpl", " Calling start listening while already started")
//            return
//        }
        messageListeners =
            database.ref.child("chats")
                .child(senderRoom).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<MessageModel>()
                        snapshot.children.forEach { children ->
                            val messageModel = children.getValue(MessageModel::class.java)
                            messageModel?.let(list::add)
                        }
                       // listOfMessages.onNext(fakeMessages())
                        listOfMessages.onNext(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // TODO
                    }
                })
    }

    //TODO: Store in room database
    override fun observeListOfMessages(): Observable<List<MessageModel>> = listOfMessages

    override fun getSenderUid(): String? { return firebaseAuth.uid }

    override fun sendMessage(input: String, senderRoom: String, receiverRoom: String, model: MessageModel) {
        if (sendMessageListeners != null) {
            Log.w("Message Repo send:", " Calling start listening while already started")
            return
        }

        database.ref.child("chats")
            .child(senderRoom)
            .push()
            .setValue(model)
            .addOnSuccessListener {
                database.ref.child("chats")
                    .child(receiverRoom)
                    .push()
                    .setValue(model)
                    .addOnSuccessListener {

                    }
            }
    }

    override fun stopListeningToMessages(senderRoom: String) {
        messageListeners?.let { database.ref.child("chats").child(senderRoom).removeEventListener(it) }
    }

    override fun stopListeningToSendMessages(senderRoom: String) {
        sendMessageListeners?.let { database.ref.child("chats").child(senderRoom).removeEventListener(it) }
    }
}
