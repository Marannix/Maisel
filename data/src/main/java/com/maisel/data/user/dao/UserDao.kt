package com.maisel.data.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maisel.data.user.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<UserEntity>)

    @Query("select * from user")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("select * from user where user_id = :userId")
    fun getUser(userId: String): Flow<UserEntity>

    @Query("DELETE FROM user")
    fun deleteAllUsers()
}
