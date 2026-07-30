package com.moon.pharm.domain.alarm

import com.moon.pharm.domain.model.medication.RepeatType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDateTime

class MedicationAlarmNextTriggerCalculatorTest {

    @Test
    fun `daily alarm schedules tomorrow when today's time has passed`() {
        val next = MedicationAlarmNextTriggerCalculator.nextTriggerAt(
            alarm = alarm(repeatType = RepeatType.DAILY),
            now = LocalDateTime.of(2026, 7, 30, 9, 30)
        )

        assertEquals(LocalDateTime.of(2026, 7, 31, 9, 0), next)
    }

    @Test
    fun `weekly alarm schedules the next selected weekday`() {
        val next = MedicationAlarmNextTriggerCalculator.nextTriggerAt(
            alarm = alarm(repeatType = RepeatType.WEEKLY, weeklyDays = setOf(3)),
            now = LocalDateTime.of(2026, 7, 30, 8, 0)
        )

        assertEquals(LocalDateTime.of(2026, 8, 5, 9, 0), next)
    }

    @Test
    fun `period alarm does not schedule after its end date`() {
        val next = MedicationAlarmNextTriggerCalculator.nextTriggerAt(
            alarm = alarm(repeatType = RepeatType.PERIOD, endDate = 0L),
            now = LocalDateTime.of(2026, 7, 30, 8, 0)
        )

        assertNull(next)
    }

    private fun alarm(
        repeatType: RepeatType,
        weeklyDays: Set<Int> = emptySet(),
        endDate: Long? = null
    ) = MedicationAlarm(
        requestCode = 1,
        medicationId = "medication",
        name = "약",
        dosage = "1정",
        time = "09:00",
        isGrouped = false,
        repeatType = repeatType,
        weeklyDays = weeklyDays,
        startDate = null,
        endDate = endDate
    )
}
