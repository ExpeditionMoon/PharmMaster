package com.moon.pharm.profile.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute
@Serializable
data object SignUpRoute
@Serializable
data object ProfileRoute
@Serializable
data object SettingsRoute
@Serializable
data object MedicationRoute
@Serializable
data class MedicationCreateRoute(
    val scannedMedicationNames: List<String> = emptyList(),
    val medicationId: String? = null,
    val isPrescriptionReview: Boolean = false
)
@Serializable
data object MedicationHistoryRoute
