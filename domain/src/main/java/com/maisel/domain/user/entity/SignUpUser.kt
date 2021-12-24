package com.maisel.domain.user.entity

//TODO: Rename to User
data class SignUpUser(var userId: String?,
                      var username: String?,
                      var emailAddress: String?,
                      var password: String?,
                      var profilePicture : String?,
                      var lastMessage: String?) {
    constructor() : this (null, null, null, null, null, null) {

    }
}
