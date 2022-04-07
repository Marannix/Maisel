package com.maisel.data.message.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "message", primaryKeys = ["sender_id", "receiver_id"])
data class MessageEntity(
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "receiver_id") val receiverId: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "date") val date : String
)
