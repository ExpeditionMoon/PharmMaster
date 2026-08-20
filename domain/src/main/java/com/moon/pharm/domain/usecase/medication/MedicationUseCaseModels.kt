package com.moon.pharm.domain.usecase.medication

enum class MedicationTypeInput {
    PRESCRIPTION,
    OTC,
    SUPPLEMENT
}

enum class MealTimingInput {
    BEFORE_MEAL,
    DURING_MEAL,
    AFTER_MEAL,
    NONE
}

enum class MedicationScheduleBasisInput {
    FIXED_TIME,
    BEFORE_MEAL,
    AFTER_MEAL,
    AS_NEEDED
}

enum class MealSlotInput {
    BREAKFAST,
    LUNCH,
    DINNER
}

enum class MealIntervalInput {
    IMMEDIATELY,
    THIRTY_MINUTES,
    ONE_HOUR
}

enum class RepeatTypeInput {
    DAILY,
    WEEKLY,
    PERIOD
}

data class MedicationScheduleCommand(
    val scheduleId: String? = null,
    val time: String,
    val dosage: String,
    val mealTiming: MealTimingInput,
    val basis: MedicationScheduleBasisInput = MedicationScheduleBasisInput.FIXED_TIME,
    val mealSlot: MealSlotInput? = null,
    val mealInterval: MealIntervalInput = MealIntervalInput.IMMEDIATELY
)

data class SaveMedicationCommand(
    val medicationId: String? = null,
    val userId: String,
    val name: String,
    val type: MedicationTypeInput,
    val startDate: Long,
    val endDate: Long?,
    val repeatType: RepeatTypeInput,
    val weeklyDays: Set<Int> = emptySet(),
    val schedules: List<MedicationScheduleCommand>,
    val intakeGroupId: String? = null,
    val isGrouped: Boolean = false,
    val isAlarmEnabled: Boolean = true,
    val status: MedicationStatusInput = MedicationStatusInput.ACTIVE
)

enum class MedicationStatusInput {
    ACTIVE,
    PAUSED,
    ENDED
}

data class ChangeMedicationStatusCommand(
    val userId: String,
    val medicationId: String,
    val status: MedicationStatusInput,
    val changedAt: Long
)

data class DeleteMedicationCommand(
    val userId: String,
    val medicationId: String
)

data class ToggleIntakeCommand(
    val userId: String,
    val medicationId: String,
    val scheduleId: String,
    val recordDate: String,
    val isTaken: Boolean,
    val takenTime: Long? = null
)

data class CompleteMedicationGroupCommand(
    val userId: String,
    val items: List<MedicationGroupIntakeItem>,
    val recordDate: String,
    val takenTime: Long
)

data class MedicationGroupIntakeItem(
    val medicationId: String,
    val scheduleId: String
)

data class MedicationScheduleItem(
    val medicationId: String,
    val intakeGroupId: String? = null,
    val scheduleId: String,
    val name: String,
    val type: MedicationTypeInput,
    val repeatType: RepeatTypeInput,
    val time: String,
    val dosage: String,
    val mealTiming: MealTimingInput,
    val isPaused: Boolean,
    val isTaken: Boolean,
    val takenTime: Long? = null,
    val isAlarmEnabled: Boolean = true
)

data class MedicationHistoryItem(
    val recordId: String,
    val medicationId: String,
    val scheduleId: String,
    val recordDate: String,
    val isTaken: Boolean,
    val takenTime: Long? = null,
    val medicationName: String,
    val time: String
)
