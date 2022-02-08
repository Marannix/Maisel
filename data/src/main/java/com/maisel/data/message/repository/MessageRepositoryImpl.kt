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
                            val messageModel = children.getValue(MessageModel::class.java)
                            messageModel?.let(list::add)
                        }
                        listOfMessages.onNext(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // TODO
                    }
                })
    }

    override fun observeListOfMessages(): Observable<List<MessageModel>> = listOfMessages

    override fun stopListeningToMessages(senderRoom: String) {
        messageListeners?.let { database.ref.child("chats").child(senderRoom).removeEventListener(it) }
    }

    override fun getSenderUid(): String? { return firebaseAuth.uid }
}
