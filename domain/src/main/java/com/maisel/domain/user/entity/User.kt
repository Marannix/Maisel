package com.maisel.domain.user.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(var userId: String?,
                var username: String?,
                var emailAddress: String?,
                var password: String?,
                var profilePicture : String?,
                var lastMessage: String?) : Parcelable {
    constructor() : this (null, null, null, null, null, null) {

    }
}
