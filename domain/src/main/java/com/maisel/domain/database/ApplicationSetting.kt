package com.maisel.domain.database

import com.maisel.domain.user.entity.User
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationSetting(
    val hasSeenShowcase: Boolean,
    val isLoggedIn: Boolean,
    val user: User?,
) {

    companion object {
        val default = ApplicationSetting(
            hasSeenShowcase = false,
            isLoggedIn = false,
            user = null,
        )
    }
}
