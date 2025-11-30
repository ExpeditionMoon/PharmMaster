package com.moon.pharm.component_ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentNavigationRoute : PharmNavigation {
    @Serializable
    data object HomeTab : ContentNavigationRoute
    @Serializable
    data object MedicationTab : ContentNavigationRoute
    @Serializable
    data object Consult : ContentNavigationRoute
    @Serializable
    data object Profile : ContentNavigationRoute
}