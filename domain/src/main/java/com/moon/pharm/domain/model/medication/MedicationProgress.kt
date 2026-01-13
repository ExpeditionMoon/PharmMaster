package com.moon.pharm.domain.model.medication

data class MedicationProgress(
    val ratio: Float,
    val completed: Int,
    val total: Int
)