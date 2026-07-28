package com.moon.pharm.consult.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MyConsultListRoute
@Serializable
data object ConsultGraphRoute
@Serializable
data class ConsultWriteGraphRoute(val consultId: String? = null)
@Serializable
data object ConsultRoute
@Serializable
data object ConsultWriteRoute
@Serializable
data object ConsultPharmacistRoute
@Serializable
data object ConsultConfirmRoute
@Serializable
data class ConsultDetailRoute(val id: String)
