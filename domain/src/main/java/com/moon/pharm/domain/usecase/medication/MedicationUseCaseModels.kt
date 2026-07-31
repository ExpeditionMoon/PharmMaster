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

enum class RepeatTypeInput {
    DAILY,
    WEEKLY,
    PERIOD
}

data class MedicationScheduleCommand(
    val scheduleId: String? = null,
    val time: String,
    val dosage: String,
    val mealTiming: MealTimingInput
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
    val isGrouped: Boolean,
    val isAlarmEnabled: Boolean = true
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

data class ToggleIntakeCommand(
    val userId: String,
    val medicationId: String,
    val scheduleId: String,
    val recordDate: String,
    val isTaken: Boolean,
    val takenTime: Long? = null
)

data class MedicationScheduleItem(
    val medicationId: String,
    val scheduleId: String,
    val name: String,
    val type: MedicationTypeInput,
    val repeatType: RepeatTypeInput,
    val time: String,
    val dosage: String,
    val mealTiming: MealTimingInput,
    val isPaused: Boolean,
    val isTaken: Boolean
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
