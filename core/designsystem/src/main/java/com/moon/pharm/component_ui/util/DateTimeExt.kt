package com.moon.pharm.component_ui.util

import com.moon.pharm.component_ui.common.ASIA_SEOUL_ZONE
import com.moon.pharm.component_ui.common.DATE_PATTERN
import com.moon.pharm.component_ui.common.DATE_TIME_PATTERN
import com.moon.pharm.component_ui.common.FORMAT_TIME_HH_MM
import com.moon.pharm.component_ui.common.TIME_PATTERN
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// ZoneId
private val SEOUL_ZONE = ZoneId.of(ASIA_SEOUL_ZONE)

// Formatters
private val yearMonthDayFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.KOREAN)
private val hourMinuteFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.KOREAN)
private val dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.KOREAN)
private val yearMonthDisplayFormatter = DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREAN)
private val dateDisplayFormatter = DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN)

/**
 * 타임스탬프(Millis)를 날짜+시간 문자열로 변환
 */
fun Long?.toDisplayDateTimeString(): String {
    if (this == null) return ""
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), SEOUL_ZONE)
        .format(dateTimeFormatter)
}

/**
 * 타임스탬프(Millis)를 날짜 문자열로 변환
 */
fun Long?.toDisplayDateString(): String {
    if (this == null) return ""
    return Instant.ofEpochMilli(this)
        .atZone(SEOUL_ZONE)
        .toLocalDate()
        .format(yearMonthDayFormatter)
}

/**
 * 타임스탬프(Millis)를 시간 문자열로 변환
 */
fun Long?.toDisplayTimeString(): String {
    if (this == null) return ""
    return Instant.ofEpochMilli(this)
        .atZone(SEOUL_ZONE)
        .toLocalTime()
        .format(hourMinuteFormatter)
}

/**
 * 총 분(Total Minutes)을 UI용 시간 문자열로 변환
 */
fun Long?.toMinuteTimeUiString(): String {
    if (this == null) return ""
    val totalMinutes = this.toInt()
    val hour = totalMinutes / 60
    val minute = totalMinutes % 60

    return LocalTime.of(hour, minute).format(hourMinuteFormatter)
}

/**
 * 알람 스케줄러 및 DB 저장을 위한 기계용 시간 포맷 (HH:mm)
 */
fun Long?.toScheduleTimeString(): String {
    val totalMinutes = (this ?: 0L).toInt()
    val hour = totalMinutes / 60
    val minute = totalMinutes % 60

    return String.format(Locale.US, FORMAT_TIME_HH_MM, hour, minute)
}

// MedicationHistory Extensions

/**
 * YearMonth를 DB 조회용 문자열(yyyy-MM)로 변환
 */
fun YearMonth.toQueryString(): String {
    return this.toString()
}

/**
 * YearMonth를 UI 표시용 문자열(yyyy년 M월)로 변환
 */
fun YearMonth.toDisplayString(): String {
    return this.format(yearMonthDisplayFormatter)
}

/**
 * LocalDate를 DB 조회/저장용 문자열(yyyy-MM-dd)로 변환
 */
fun LocalDate.toQueryString(): String {
    return this.toString()
}

/**
 * LocalDate를 UI 표시용 문자열(M월 d일 (E))로 변환
 */
fun LocalDate.toDisplayString(): String {
    return this.format(dateDisplayFormatter)
}

/**
 * 문자열(yyyy-MM-dd)을 LocalDate로 파싱
 */
fun String.toLocalDate(): LocalDate {
    return try {
        LocalDate.parse(this)
    } catch (e: Exception) {
        LocalDate.now()
    }
}