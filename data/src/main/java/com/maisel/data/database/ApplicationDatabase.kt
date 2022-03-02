package com.maisel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maisel.data.user.dao.UserDao
import com.maisel.data.user.entity.UserEntity

@Database(
    entities = [UserEntity::class], version = 1
)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
