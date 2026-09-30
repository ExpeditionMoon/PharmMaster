package com.moon.pharm.home.viewmodel

data class HomeUiState(
    val nextMedicationReminder: HomeMedicationReminder? = null,
    val remainingReminderCount: Int = 0,
    val medicationRegistrationStatus: HomeMedicationRegistrationStatus? = null,
    val todayMedicationCount: Int = 0,
    val todayCompletedCount: Int = 0,
    val lastCompletedTime: Long? = null,
    val isTodayMedicationLoaded: Boolean = false,
    val weeklyMedicationCount: Int? = null,
    val weeklyCompletedCount: Int? = null,
    val isMedicationDataLoadFailed: Boolean = false
) {
    val weeklyAdherencePercent: Int?
        get() {
            val medicationCount = weeklyMedicationCount ?: return null
            val completedCount = weeklyCompletedCount ?: return null

            return if (medicationCount == 0) {
                null
            } else {
                (completedCount * 100 / medicationCount).coerceIn(0, 100)
            }
        }

    val medicationSummary: HomeMedicationSummary
        get() = when {
            isMedicationDataLoadFailed -> HomeMedicationSummary.LoadFailed
            medicationRegistrationStatus == HomeMedicationRegistrationStatus.NoMedication -> {
                HomeMedicationSummary.NoRegisteredMedication
            }
            medicationRegistrationStatus == HomeMedicationRegistrationStatus.NoOngoingMedication -> {
                HomeMedicationSummary.NoOngoingMedication
            }
            medicationRegistrationStatus == HomeMedicationRegistrationStatus.Paused -> {
                HomeMedicationSummary.PausedMedication
            }
            medicationRegistrationStatus == HomeMedicationRegistrationStatus.StartsLater -> {
                HomeMedicationSummary.MedicationStartsLater
            }
            medicationRegistrationStatus == null || !isTodayMedicationLoaded -> HomeMedicationSummary.Loading
            todayMedicationCount == 0 -> HomeMedicationSummary.NoTodayMedication
            else -> HomeMedicationSummary.TodayMedication(
                totalCount = todayMedicationCount,
                completedCount = todayCompletedCount,
                lastCompletedTime = lastCompletedTime
            )
        }
}

enum class HomeMedicationRegistrationStatus {
    NoMedication,
    NoOngoingMedication,
    Paused,
    StartsLater,
    Ongoing
}

sealed interface HomeMedicationSummary {
    data object Loading : HomeMedicationSummary
    data object NoRegisteredMedication : HomeMedicationSummary
    data object NoOngoingMedication : HomeMedicationSummary
    data object PausedMedication : HomeMedicationSummary
    data object MedicationStartsLater : HomeMedicationSummary
    data object NoTodayMedication : HomeMedicationSummary
    data class TodayMedication(
        val totalCount: Int,
        val completedCount: Int,
        val lastCompletedTime: Long?
    ) : HomeMedicationSummary
    data object LoadFailed : HomeMedicationSummary
}

data class HomeMedicationReminder(
    val time: String,
    val representativeName: String,
    val medicationCount: Int
) {
    val displayName: String
        get() = if (medicationCount > 1) "$representativeName 외 ${medicationCount - 1}개" else representativeName
}
