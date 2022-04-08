package com.maisel.data.message.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maisel.data.message.entity.RecentMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecentMessages(recentMessages: List<RecentMessageEntity>)

    @Query("select * from recent_message")
    fun getRecentMessages(): Flow<List<RecentMessageEntity>>
}
