package com.moon.pharm.domain.usecase.medication

import java.time.LocalTime
import java.time.format.DateTimeFormatter

/** Creates concrete daily schedules from a meal-based prescription instruction. */
object MedicationScheduleCommandFactory {
    fun create(
        dosage: String,
        mealTiming: MealTimingInput,
        basis: MedicationScheduleBasisInput,
        mealSlots: Set<MealSlotInput> = emptySet(),
        mealInterval: MealIntervalInput = MealIntervalInput.IMMEDIATELY,
        fixedTime: String = DEFAULT_FIXED_TIME
    ): List<MedicationScheduleCommand> {
        if (basis == MedicationScheduleBasisInput.AS_NEEDED) {
            return listOf(
                MedicationScheduleCommand(
                    time = fixedTime,
                    dosage = dosage,
                    mealTiming = mealTiming,
                    basis = basis,
                    mealInterval = mealInterval
                )
            )
        }

        if (basis == MedicationScheduleBasisInput.FIXED_TIME) {
            return listOf(
                MedicationScheduleCommand(
                    time = fixedTime,
                    dosage = dosage,
                    mealTiming = mealTiming,
                    basis = basis,
                    mealInterval = mealInterval
                )
            )
        }

        return mealSlots
            .ifEmpty { DEFAULT_MEAL_SLOTS }
            .sortedBy(MealSlotInput::ordinal)
            .map { slot ->
                MedicationScheduleCommand(
                    time = slot.defaultTime().adjustedBy(basis, mealInterval),
                    dosage = dosage,
                    mealTiming = mealTiming,
                    basis = basis,
                    mealSlot = slot,
                    mealInterval = mealInterval
                )
            }
    }

    private fun MealSlotInput.defaultTime(): LocalTime = when (this) {
        MealSlotInput.BREAKFAST -> LocalTime.of(8, 0)
        MealSlotInput.LUNCH -> LocalTime.of(12, 30)
        MealSlotInput.DINNER -> LocalTime.of(18, 30)
    }

    private fun LocalTime.adjustedBy(
        basis: MedicationScheduleBasisInput,
        interval: MealIntervalInput
    ): String {
        val minutes = when (interval) {
            MealIntervalInput.IMMEDIATELY -> 0L
            MealIntervalInput.THIRTY_MINUTES -> 30L
            MealIntervalInput.ONE_HOUR -> 60L
        }
        val adjusted = when (basis) {
            MedicationScheduleBasisInput.BEFORE_MEAL -> minusMinutes(minutes)
            MedicationScheduleBasisInput.AFTER_MEAL -> plusMinutes(minutes)
            MedicationScheduleBasisInput.FIXED_TIME,
            MedicationScheduleBasisInput.AS_NEEDED -> this
        }
        return adjusted.format(TIME_FORMATTER)
    }

    private val DEFAULT_MEAL_SLOTS = setOf(MealSlotInput.BREAKFAST)
    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private const val DEFAULT_FIXED_TIME = "09:00"
}
