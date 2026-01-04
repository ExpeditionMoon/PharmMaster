package com.moon.pharm.domain.model

data class MedicationItem(
    val id: String = "",
    val name: String,
    val dosage: String?,
    val type: MedicationType,
    val startDate: Long?,
    val endDate: Long?,
    val noEndDate: Boolean,
    val alarmTime: Long?,
    val mealTiming: MealTiming,
    val repeatType: RepeatType,
    val isTaken: Boolean = false
)

data class MedicationTimeGroup(
    val time: Long?,
    val items: List<MedicationItem>
)

enum class MedicationType(val label: String) {
    PRESCRIPTION("처방약"),
    OTC("일반약"),
    SUPPLEMENT("비타민/영양제")
}

enum class MealTiming(val label: String) {
    BEFORE_MEAL("식전"),
    DURING_MEAL("식사 중"),
    AFTER_MEAL("식후")
}

enum class RepeatType(val label: String) {
    DAILY("매일"),
    WEEKLY("매주"),
    PERIOD("특정 기간")
}
