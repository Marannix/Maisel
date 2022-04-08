package com.maisel.data.user.mapper

import com.maisel.data.user.entity.UserEntity
import com.maisel.domain.user.entity.User

fun UserEntity.toDomain(): User {
    return User(
        userId = userId,
        username = username,
        emailAddress = emailAddress,
        password = password,
        profilePicture = profilePicture,
        lastMessage = lastMessage
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = userId!!,
        username = username,
        emailAddress = emailAddress,
        password = password,
        profilePicture = profilePicture,
        lastMessage = lastMessage
    )
}
