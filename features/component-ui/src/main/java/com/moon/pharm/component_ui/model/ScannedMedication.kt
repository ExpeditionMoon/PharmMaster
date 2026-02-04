package com.moon.pharm.component_ui.model

import kotlinx.serialization.Serializable

@Serializable
data class ScannedMedication(
    val name: String,
    val dailyCount: Int
)