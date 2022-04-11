package com.maisel.data.message.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "recent_message", primaryKeys = ["user_id"])
data class RecentMessageEntity(
    @ColumnInfo(name = "message_id") val messageId: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "receiver_id") val receiverId: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "date") val date : String
)
