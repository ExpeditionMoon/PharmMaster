package com.moon.pharm.component_ui.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.moon.pharm.component_ui.common.ASIA_SEOUL_ZONE
import com.moon.pharm.component_ui.common.DATE_PATTERN
import com.moon.pharm.component_ui.common.DATE_TIME_PATTERN
import com.moon.pharm.component_ui.common.FORMAT_TIME_HH_MM
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

// Formatters
@get:RequiresApi(Build.VERSION_CODES.O)
private val yearMonthDayFormatter get() = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.KOREAN)

@get:RequiresApi(Build.VERSION_CODES.O)
private val hourMinuteFormatter get() = DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.KOREAN)

@get:RequiresApi(Build.VERSION_CODES.O)
private val dateTimeFormatter get() = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.KOREAN)

private fun getLegacyFormatter(pattern: String): java.text.SimpleDateFormat {
    return java.text.SimpleDateFormat(pattern, Locale.KOREAN)
}

/**
 * 타임스탬프(Millis)를 날짜+시간 문자열로 변환
 */
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

/**
 * 타임스탬프(Millis)를 날짜 문자열로 변환
 */
@SuppressLint("NewApi")
fun Long?.toDisplayDateString(): String {
    if (this == null) return ""
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.ofEpochMilli(this).atZone(SEOUL_ZONE).toLocalDate().format(yearMonthDayFormatter)
    } else {
        getLegacyFormatter(DATE_PATTERN).format(Date(this))
    }
}

/**
 * 타임스탬프(Millis)를 시간 문자열로 변환
 */
@SuppressLint("NewApi")
fun Long?.toDisplayTimeString(): String {
    if (this == null) return ""
    val totalSeconds = this / 1000
    val hour = (totalSeconds / 3600).toInt()
    val minute = ((totalSeconds % 3600) / 60).toInt()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.ofEpochMilli(this)
            .atZone(SEOUL_ZONE)
            .toLocalTime()
            .format(hourMinuteFormatter)
    } else {
        getLegacyFormatter(TIME_PATTERN).format(Date(this))
    }
}

/**
 * 총 분(Total Minutes)을 UI용 시간 문자열로 변환
 */
@SuppressLint("NewApi")
fun Long?.toMinuteTimeUiString(): String {
    if (this == null) return ""

    val totalMinutes = this.toInt()
    val hour = totalMinutes / 60
    val minute = totalMinutes % 60

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

/**
 * 알람 스케줄러 및 DB 저장을 위한 기계용 시간 포맷
 */
@SuppressLint("NewApi")
fun Long?.toScheduleTimeString(): String {
    val totalMinutes = (this ?: 0L).toInt()
    val hour = totalMinutes / 60
    val minute = totalMinutes % 60

    return String.format(java.util.Locale.US, FORMAT_TIME_HH_MM, hour, minute)
}