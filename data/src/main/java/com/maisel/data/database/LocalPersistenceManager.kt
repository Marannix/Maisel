package com.maisel.data.database

import com.maisel.domain.user.entity.User

interface LocalPersistenceManager {

    fun setLoggedInUser(user: User?)
    fun getLoggedInUser() : User?
    fun removeUser()
    fun setViewedShowcase(value: Boolean)
    fun hasViewedShowcase() : Boolean
}
