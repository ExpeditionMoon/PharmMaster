package com.moon.pharm.domain.model.medication

data class Medication(
    val id: String,
    val userId: String,
    val name: String,
    val type: MedicationType,
    val startDate: Long?,
    val endDate: Long?,
    val repeatType: RepeatType,
    val memo: String? = null,
    val schedules: List<MedicationSchedule>,

    val prescriptionImageUrl: String? = null,
    val isGrouped: Boolean = false
)