package com.moon.pharm.component_ui.util

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalTime

@SuppressLint("NewApi")
fun LocalDate?.toDisplayDateString(): String {
    return this?.format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")) ?: ""
}

@SuppressLint("NewApi")
fun Long?.toLocalDate(): LocalDate {
    val millis = this ?: System.currentTimeMillis()
    return java.time.Instant.ofEpochMilli(millis)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
}

@SuppressLint("NewApi")
fun LocalTime?.toDisplayTimeString(): String {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("a hh:mm", java.util.Locale.KOREAN)
    return this?.format(formatter) ?: "시간 미지정"
}