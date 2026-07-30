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
            val isWithinEndDate = endDate == null || !date.isAfter(endDate)
            val matchesRepeat = when (alarm.repeatType) {
                RepeatType.DAILY, RepeatType.PERIOD -> true
                RepeatType.WEEKLY -> date.dayOfWeek.value in alarm.weeklyDays.orDefault(
                    startDate ?: now.toLocalDate()
                )
            }

            if (isAfterNow && isWithinEndDate && matchesRepeat) return candidate
            date = date.plusDays(1)
        }
        return null
    }

    private fun String.toLocalTimeOrNull(): LocalTime? = runCatching {
        LocalTime.parse(this)
    }.getOrNull()

    private fun Long.toLocalDate(zoneId: ZoneId): LocalDate =
        Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()

    private fun Set<Int>.orDefault(defaultDate: LocalDate): Set<Int> =
        filter { it in 1..7 }.toSet().ifEmpty { setOf(defaultDate.dayOfWeek.value) }
}
