package com.moon.pharm.home.viewmodel

data class HomeUiState(
    val nextMedicationReminder: HomeMedicationReminder? = null,
    val remainingReminderCount: Int = 0,
    val todayMedicationCount: Int = 0,
    val todayCompletedCount: Int = 0,
    val weeklyMedicationCount: Int = 0,
    val weeklyCompletedCount: Int = 0
) {
    val weeklyAdherencePercent: Int
        get() = if (weeklyMedicationCount == 0) 0 else {
            (weeklyCompletedCount * 100 / weeklyMedicationCount).coerceIn(0, 100)
        }
}

data class HomeMedicationReminder(
    val time: String,
    val representativeName: String,
    val medicationCount: Int
) {
    val displayName: String
        get() = if (medicationCount > 1) "$representativeName 외 ${medicationCount - 1}개" else representativeName
}
