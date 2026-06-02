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
    val time: String,
    val dosage: String,
    val mealTiming: MealTimingInput
)

data class SaveMedicationCommand(
    val userId: String,
    val name: String,
    val type: MedicationTypeInput,
    val startDate: Long,
    val endDate: Long?,
    val repeatType: RepeatTypeInput,
    val schedules: List<MedicationScheduleCommand>,
    val isGrouped: Boolean
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
