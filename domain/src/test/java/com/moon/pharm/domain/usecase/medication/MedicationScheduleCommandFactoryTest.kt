package com.moon.pharm.domain.usecase.medication

import org.junit.Assert.assertEquals
import org.junit.Test

class MedicationScheduleCommandFactoryTest {

    @Test
    fun `creates breakfast and dinner schedules thirty minutes after meals`() {
        val schedules = MedicationScheduleCommandFactory.create(
            dosage = "1정",
            mealTiming = MealTimingInput.AFTER_MEAL,
            basis = MedicationScheduleBasisInput.AFTER_MEAL,
            mealSlots = setOf(MealSlotInput.BREAKFAST, MealSlotInput.DINNER),
            mealInterval = MealIntervalInput.THIRTY_MINUTES
        )

        assertEquals(listOf("08:30", "19:00"), schedules.map(MedicationScheduleCommand::time))
    }

    @Test
    fun `creates three concrete schedules for three selected meals`() {
        val schedules = MedicationScheduleCommandFactory.create(
            dosage = "1정",
            mealTiming = MealTimingInput.AFTER_MEAL,
            basis = MedicationScheduleBasisInput.AFTER_MEAL,
            mealSlots = MealSlotInput.entries.toSet()
        )

        assertEquals(3, schedules.size)
        assertEquals(
            listOf(MealSlotInput.BREAKFAST, MealSlotInput.LUNCH, MealSlotInput.DINNER),
            schedules.mapNotNull(MedicationScheduleCommand::mealSlot)
        )
    }

    @Test
    fun `creates a recordable optional reminder schedule for as needed medication`() {
        val schedules = MedicationScheduleCommandFactory.create(
            dosage = "1정",
            mealTiming = MealTimingInput.NONE,
            basis = MedicationScheduleBasisInput.AS_NEEDED
        )

        assertEquals(1, schedules.size)
        assertEquals(MedicationScheduleBasisInput.AS_NEEDED, schedules.single().basis)
    }
}
