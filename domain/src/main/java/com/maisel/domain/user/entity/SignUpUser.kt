package com.maisel.domain.user.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//TODO: Rename to User
@Parcelize
data class SignUpUser(var userId: String?,
                      var username: String?,
                      var emailAddress: String?,
                      var password: String?,
                      var profilePicture : String?,
                      var lastMessage: String?) : Parcelable {
    constructor() : this (null, null, null, null, null, null) {

    }
}
