package com.moon.pharm.component_ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
private val SEOUL_ZONE = ZoneId.of("Asia/Seoul")

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate?.toDisplayDateString(): String {
    return this?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) ?: ""
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long?.toLocalDate(): LocalDate {
    val millis = this ?: System.currentTimeMillis()
    return Instant.ofEpochMilli(millis)
        .atZone(SEOUL_ZONE)
        .toLocalDate()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime?.toDisplayTimeString(): String {
    val formatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN)
    return this?.format(formatter) ?: "시간 미지정"
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime?.toDisplayString(): String {
    return this?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "시간 선택"
}