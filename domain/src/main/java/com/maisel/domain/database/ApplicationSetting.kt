package com.maisel.domain.database

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationSetting(
    val hasSeenShowcase: Boolean,
    val isLoggedIn: Boolean,
) {

    companion object {
        val default = ApplicationSetting(
            hasSeenShowcase = false,
            isLoggedIn = false,
        )
    }
}
