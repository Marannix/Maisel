package com.maisel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maisel.data.message.dao.MessageDao
import com.maisel.data.message.dao.RecentMessageDao
import com.maisel.data.message.entity.MessageEntity
import com.maisel.data.message.entity.RecentMessageEntity
import com.maisel.data.user.dao.UserDao
import com.maisel.data.user.entity.UserEntity

@Database(
    entities = [UserEntity::class, RecentMessageEntity::class, MessageEntity::class], version = 1
)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recentMessageDao(): RecentMessageDao
    abstract fun messageDao(): MessageDao
}
