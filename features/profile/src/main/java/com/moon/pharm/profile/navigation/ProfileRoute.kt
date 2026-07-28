package com.moon.pharm.profile.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute
@Serializable
data object SignUpRoute
@Serializable
data object ProfileRoute
@Serializable
data object MedicationRoute
@Serializable
data class MedicationCreateRoute(val scannedMedicationNames: List<String> = emptyList())
@Serializable
data object MedicationHistoryRoute
