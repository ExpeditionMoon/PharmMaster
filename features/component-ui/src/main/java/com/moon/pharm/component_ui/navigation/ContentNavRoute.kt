package com.moon.pharm.component_ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentNavigationRoute : PharmNavigation {
    @Serializable
    data object HomeTab : ContentNavigationRoute
    @Serializable
    data object MedicationTab : ContentNavigationRoute
    @Serializable
    data object ConsultTab : ContentNavigationRoute
    @Serializable
    data object ProfileTab : ContentNavigationRoute
    @Serializable
    data object ConsultWrite : ContentNavigationRoute
}