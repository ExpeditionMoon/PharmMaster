package com.moon.pharm.component_ui.navigation

import com.moon.pharm.component_ui.model.ScannedMedication
import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentNavigationRoute : PharmNavigation {
    // 공통 및 인증
    @Serializable
    data object MainBase : ContentNavigationRoute
    @Serializable
    data object LoginScreen : ContentNavigationRoute
    @Serializable
    data object SignUpScreen : ContentNavigationRoute
    @Serializable
    data object HomeTab : ContentNavigationRoute

    // 복약 관리
    @Serializable
    data object MedicationTab : ContentNavigationRoute
    @Serializable
    data class MedicationTabCreateScreen(
        val scannedList: List<ScannedMedication> = emptyList()
    ) : ContentNavigationRoute
    @Serializable
    data object MedicationTabHistoryScreen : ContentNavigationRoute

    // 상담
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

    // 내정보 및 기타
    @Serializable
    data object ProfileTab : ContentNavigationRoute
    @Serializable
    object MyConsultList : ContentNavigationRoute
    @Serializable
    data object PrescriptionCapture : ContentNavigationRoute
    @Serializable
    data object PrescriptionCamera : ContentNavigationRoute

    // 약 검색
    @Serializable
    data object Search : ContentNavigationRoute
}