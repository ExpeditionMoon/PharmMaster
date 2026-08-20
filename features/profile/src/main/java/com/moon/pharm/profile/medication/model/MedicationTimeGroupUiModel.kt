package com.moon.pharm.profile.medication.model

data class MedicationTimeGroupUiModel(
    val time: String?,
    val items: List<TodayMedicationUiModel>,
    val id: String = time.orEmpty(),
    val representativeName: String = items.firstOrNull()?.name.orEmpty(),
    val completedCount: Int = items.count(TodayMedicationUiModel::isTaken)
) {
    val isIntakeGroup: Boolean get() = items.firstOrNull()?.intakeGroupId != null
    val totalCount: Int get() = items.size
}
