package com.moon.pharm.component_ui.util

import android.annotation.SuppressLint
import java.time.LocalDate

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