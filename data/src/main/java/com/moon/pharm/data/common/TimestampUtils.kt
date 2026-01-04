package com.moon.pharm.data.common

import com.google.firebase.Timestamp
import java.util.Date

fun Timestamp?.toLong(): Long {
    return this?.toDate()?.time ?: 0L
}

fun Long?.toTimestamp(): Timestamp? {
    return this?.let { Timestamp(Date(it)) }
}

fun parseAlarmTimeToLong(timeStr: String?): Long? {
    if (timeStr.isNullOrEmpty()) return null
    return try {
        if (timeStr.contains(":")) {
            val parts = timeStr.split(":")
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            (hours * 60) + minutes
        } else {
            timeStr.toLong()
        }
    } catch (e: Exception) {
        null
    }
}