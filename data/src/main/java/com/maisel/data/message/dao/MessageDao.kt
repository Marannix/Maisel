package com.maisel.data.message.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maisel.data.message.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(recentMessages: List<MessageEntity>)

    @Query("select * from message where sender_id = :senderId and receiver_id = :receiverId or sender_id = :receiverId and receiver_id = :senderId")
    fun getMessages(senderId: String, receiverId: String): Flow<List<MessageEntity>>

    @Query("DELETE FROM message")
    fun deleteAllMessages()
}
