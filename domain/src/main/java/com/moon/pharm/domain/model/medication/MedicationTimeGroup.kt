package com.moon.pharm.domain.model.medication

data class MedicationTimeGroup(
    val time: String?,
    val items: List<TodayMedicationUiModel>
)