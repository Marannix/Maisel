package com.maisel.data.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user", primaryKeys = ["user_id"])
data class UserEntity(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "email_address") val emailAddress: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "profile_picture") val profilePicture : String?,
    @ColumnInfo(name = "last_message") val lastMessage: String?
)
