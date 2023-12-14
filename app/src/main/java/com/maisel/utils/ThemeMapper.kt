package com.maisel.utils

import com.maisel.R
import com.maisel.domain.database.AppTheme
import javax.inject.Inject

class ThemeMapper @Inject constructor() {

    fun mapEnumToString(appTheme: AppTheme?): String? {
        return when (appTheme) {
            AppTheme.LIGHT_MODE -> AppTheme.LIGHT_MODE.name
            AppTheme.DARK_MODE -> AppTheme.DARK_MODE.name
            AppTheme.SYSTEM_DEFAULT -> AppTheme.SYSTEM_DEFAULT.name
            else -> null
        }
    }

    fun mapStringToEnum(isDarkMode: String): AppTheme {
        return when (isDarkMode) {
            AppTheme.LIGHT_MODE.name-> AppTheme.LIGHT_MODE
            AppTheme.DARK_MODE.name -> AppTheme.DARK_MODE
            AppTheme.SYSTEM_DEFAULT.name -> AppTheme.SYSTEM_DEFAULT
            else -> { throw IllegalArgumentException("Invalid theme")}
        }
    }
}
