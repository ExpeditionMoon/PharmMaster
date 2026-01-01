package com.moon.pharm.component_ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
private val SEOUL_ZONE = ZoneId.of("Asia/Seoul")

@RequiresApi(Build.VERSION_CODES.O)
val yearMonthDayFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREAN)
@RequiresApi(Build.VERSION_CODES.O)
val hourMinuteFormatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN)
@RequiresApi(Build.VERSION_CODES.O)
val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate?.toDisplayDateString() = this?.format(yearMonthDayFormatter) ?: ""
@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime?.toDisplayTimeString() = this?.format(hourMinuteFormatter) ?: ""
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime?.toDisplayDateTimeString() = this?.format(dateTimeFormatter) ?: ""

@RequiresApi(Build.VERSION_CODES.O)
fun Long?.toLocalDate(): LocalDate {
    val millis = this ?: System.currentTimeMillis()
    return Instant.ofEpochMilli(millis)
        .atZone(SEOUL_ZONE)
        .toLocalDate()
}