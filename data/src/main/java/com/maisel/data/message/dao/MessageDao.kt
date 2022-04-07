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

    @Query("select * from message")
    fun getMessages(): Flow<List<MessageEntity>>
}
