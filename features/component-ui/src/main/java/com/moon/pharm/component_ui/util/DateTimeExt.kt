package com.moon.pharm.component_ui.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.moon.pharm.component_ui.common.ASIA_SEOUL_ZONE
import com.moon.pharm.component_ui.common.DATE_PATTERN
import com.moon.pharm.component_ui.common.DATE_TIME_PATTERN
import com.moon.pharm.component_ui.common.TIME_PATTERN
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@get:RequiresApi(Build.VERSION_CODES.O)
private val SEOUL_ZONE get() = ZoneId.of(ASIA_SEOUL_ZONE)

@get:RequiresApi(Build.VERSION_CODES.O)
private val yearMonthDayFormatter get() = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.KOREAN)

@get:RequiresApi(Build.VERSION_CODES.O)
private val hourMinuteFormatter get() = DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.KOREAN)

@get:RequiresApi(Build.VERSION_CODES.O)
private val dateTimeFormatter get() = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.KOREAN)

private fun getLegacyFormatter(pattern: String): java.text.SimpleDateFormat {
    return java.text.SimpleDateFormat(pattern, Locale.KOREAN)
}

@SuppressLint("NewApi")
fun Long?.toDisplayDateTimeString(): String {
    if (this == null) return ""
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val instant = Instant.ofEpochMilli(this)
        val dateTime = LocalDateTime.ofInstant(instant, SEOUL_ZONE)
        dateTime.format(dateTimeFormatter)
    } else {
        val date = java.util.Date(this)
        getLegacyFormatter(DATE_TIME_PATTERN).format(date)
    }
}

@SuppressLint("NewApi")
fun Long?.toDisplayDateString(): String {
    if (this == null) return ""
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.ofEpochMilli(this).atZone(SEOUL_ZONE).toLocalDate().format(yearMonthDayFormatter)
    } else {
        getLegacyFormatter(DATE_PATTERN).format(Date(this))
    }
}

@SuppressLint("NewApi")
fun Long?.toDisplayTimeString(): String {
    if (this == null) return ""
    val hour = (this / 60).toInt()
    val minute = (this % 60).toInt()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalTime.of(hour, minute).format(hourMinuteFormatter)
    } else {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
        }
        getLegacyFormatter(TIME_PATTERN).format(calendar.time)
    }
}