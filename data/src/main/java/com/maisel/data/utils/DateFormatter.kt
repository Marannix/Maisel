package com.maisel.data.utils

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    fun getChatTime(timestamp: Long) : String {
        return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(timestamp)
    }
}
