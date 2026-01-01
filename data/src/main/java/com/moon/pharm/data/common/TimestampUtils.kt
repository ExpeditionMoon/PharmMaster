package com.moon.pharm.data.common

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

private val SEOUL_ZONE = ZoneId.of(ASIA_SEOUL_ZONE)

@RequiresApi(Build.VERSION_CODES.O)
fun Timestamp?.toLocalDate(): LocalDate {
    return this?.toDate()?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate() ?: LocalDate.now()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate?.toTimestamp(): Timestamp? {
    return this?.let {
        val date = Date.from(it.atStartOfDay(SEOUL_ZONE).toInstant())
        Timestamp(date)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Timestamp?.toLocalDateTime(): LocalDateTime {
    return this?.toDate()?.toInstant()
        ?.atZone(SEOUL_ZONE)
        ?.toLocalDateTime() ?: LocalDateTime.now()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime?.toTimestamp(): Timestamp? {
    return this?.let {
        val date = Date.from(it.atZone(SEOUL_ZONE).toInstant())
        Timestamp(date)
    }
}