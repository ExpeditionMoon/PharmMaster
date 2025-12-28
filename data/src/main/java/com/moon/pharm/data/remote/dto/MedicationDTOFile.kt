package com.moon.pharm.data.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class MedicationItemDto(
    @DocumentId
    val id: String = "",

    val name: String = "",
    val dosage: String? = "",
    val type: String = "",
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val noEndDate: Boolean = false,
    val alarmTime: String = "",
    val mealTiming: String = "",
    val repeatType: String = "",
    val isTaken: Boolean = false
)