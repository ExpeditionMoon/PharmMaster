package com.moon.pharm.domain.alarm

import com.moon.pharm.domain.model.medication.RepeatType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object MedicationAlarmNextTriggerCalculator {
    fun nextTriggerAt(
        alarm: MedicationAlarm,
        now: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): LocalDateTime? {
        val time = alarm.time.toLocalTimeOrNull() ?: return null
        val startDate = alarm.startDate?.toLocalDate(zoneId)
        val endDate = alarm.endDate?.toLocalDate(zoneId)
        var date = maxOf(now.toLocalDate(), startDate ?: now.toLocalDate())

        repeat(8) {
            val candidate = LocalDateTime.of(date, time)
            val isAfterNow = candidate.isAfter(now)

            if (isAfterNow && isActiveOn(alarm, date, startDate, endDate)) return candidate
            date = date.plusDays(1)
        }
        return null
    }

    fun isActiveOn(
        alarm: MedicationAlarm,
        date: LocalDate,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Boolean {
        val startDate = alarm.startDate?.toLocalDate(zoneId)
        val endDate = alarm.endDate?.toLocalDate(zoneId)
        return isActiveOn(alarm, date, startDate, endDate)
    }

    private fun isActiveOn(
        alarm: MedicationAlarm,
        date: LocalDate,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Boolean {
        if (startDate != null && date.isBefore(startDate)) return false
        if (endDate != null && date.isAfter(endDate)) return false

        return when (alarm.repeatType) {
            RepeatType.DAILY, RepeatType.PERIOD -> true
            RepeatType.WEEKLY -> date.dayOfWeek.value in alarm.weeklyDays.orDefault()
        }
    }

    private fun String.toLocalTimeOrNull(): LocalTime? = runCatching {
        LocalTime.parse(this)
    }.getOrNull()

    private fun Long.toLocalDate(zoneId: ZoneId): LocalDate =
        Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()

    private fun Set<Int>.orDefault(): Set<Int> =
        filter { it in 1..7 }.toSet().ifEmpty { (1..7).toSet() }
}
