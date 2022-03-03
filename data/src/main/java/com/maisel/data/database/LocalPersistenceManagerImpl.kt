package com.maisel.data.database

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.maisel.data.database.LocalPersistenceManagerImpl.Constants.KEY_SHOWCASE
import com.maisel.data.database.LocalPersistenceManagerImpl.Constants.KEY_USER
import com.maisel.domain.user.entity.User
import javax.inject.Inject

class LocalPersistenceManagerImpl @Inject constructor(
    private val preference: SharedPreferences,
    private val gson: Gson
) :
    LocalPersistenceManager {

    override fun setUser(user: User?) {
        preference.edit { putString(KEY_USER, gson.toJson(user).toString()) }
    }

    override fun getUser() : User? {
        val value = preference.getString(KEY_USER, "")
        return if (value != null) {
            GsonBuilder().create().fromJson(value, User::class.java)
        } else null
    }

    override fun removeUser() {
        TODO("Not yet implemented")
    }

    override fun setViewedShowcase(value: Boolean) {
        preference.edit { putBoolean(KEY_SHOWCASE, value) }
    }

    override fun hasViewedShowcase() = preference.getBoolean(KEY_SHOWCASE, false)

    object Constants {
        const val KEY_SHOWCASE = "KEY_SHOWCASE"
        const val KEY_USER = "KEY_USER"
    }
}
