package com.maisel.data.database

import com.maisel.domain.user.entity.User

interface LocalPersistenceManager {

    fun setUser(user: User?)
    fun getUser() : User?
    fun removeUser()
    fun setViewedShowcase(value: Boolean)
    fun hasViewedShowcase() : Boolean
}
