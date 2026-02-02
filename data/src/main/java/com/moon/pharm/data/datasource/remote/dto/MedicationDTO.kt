package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.moon.pharm.data.common.EMPTY_STRING

@IgnoreExtraProperties
data class MedicationDTO(
    @DocumentId
    val id: String = EMPTY_STRING,

    val userId: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val type: String = EMPTY_STRING,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val repeatType: String = EMPTY_STRING,
    val memo: String? = null,

    val schedules: List<MedicationScheduleDTO> = emptyList(),
    val prescriptionImageUrl: String? = null,
    val isGrouped: Boolean = false
)

@IgnoreExtraProperties
data class MedicationScheduleDTO(
    var id: String = EMPTY_STRING,
    var time: String = EMPTY_STRING,
    var dosage: String = EMPTY_STRING,
    var mealTiming: String = EMPTY_STRING
)