package com.maisel.data.message.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "message_id") val messageId: String,
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "receiver_id") val receiverId: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "date") val date : String,
    @ColumnInfo(name = "timestamp") val timestamp : Long
)
