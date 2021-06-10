package com.konyekokim.commons.ui

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getDateString(date: Long): String? {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        .format(Date(date * 1000L))
}

fun getDay(date: String): String? {
    var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return try {
        val newDate = dateFormat.parse(date)
        dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()
        dateFormat.format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}