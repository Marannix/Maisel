package com.maisel.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun DatabaseReference.observeValue(): Flow<DataSnapshot?> {
    return callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot)
            }
        }
        addValueEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }
}

@ExperimentalCoroutinesApi
fun DatabaseReference.observeLastMessage(): Flow<DataSnapshot?> {
    return callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot)
            }
        }
        orderByChild("timestamp")
        limitToLast(1)
        addListenerForSingleValueEvent(listener)
        awaitClose { removeEventListener(listener) }
    }
}
