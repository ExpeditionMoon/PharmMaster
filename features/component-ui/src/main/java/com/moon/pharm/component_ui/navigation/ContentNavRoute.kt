package com.moon.pharm.component_ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentNavigationRoute : PharmNavigation {
    @Serializable
    data object LoginScreen : ContentNavigationRoute
    @Serializable
    data object SignUpScreen : ContentNavigationRoute
    @Serializable
    data object HomeTab : ContentNavigationRoute
    @Serializable
    data object MedicationTab : ContentNavigationRoute
    @Serializable
    data object MedicationTabCreateScreen : ContentNavigationRoute
    @Serializable
    data object MedicationTabHistoryScreen : ContentNavigationRoute

    @Serializable
    data object ConsultGraph : ContentNavigationRoute
    @Serializable
    data object ConsultWriteGraph : ContentNavigationRoute
    @Serializable
    data object ConsultTab : ContentNavigationRoute
    @Serializable
    data object ConsultTabWriteScreen : ContentNavigationRoute
    @Serializable
    data object ConsultTabPharmacistScreen : ContentNavigationRoute
    @Serializable
    data object ConsultTabConfirmScreen : ContentNavigationRoute
    @Serializable
    data class ConsultTabDetailScreen(val id: String) : ContentNavigationRoute

    @Serializable
    data object ProfileTab : ContentNavigationRoute
    @Serializable
    object MyConsultList : ContentNavigationRoute
    @Serializable
    data object PrescriptionCapture : ContentNavigationRoute
}