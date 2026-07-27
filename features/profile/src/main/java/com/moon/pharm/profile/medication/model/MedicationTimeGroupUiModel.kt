package com.moon.pharm.profile.medication.model

data class MedicationTimeGroupUiModel(
    val time: String?,
    val items: List<TodayMedicationUiModel>
)
