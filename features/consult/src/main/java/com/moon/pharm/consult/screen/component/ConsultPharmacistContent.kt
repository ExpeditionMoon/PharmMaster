package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.component.map.PharmacySelector
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ConsultPharmacistContent(
    isMapView: Boolean,
    searchQuery: String,
    searchResults: List<Pharmacy>,
    selectedPharmacy: Pharmacy?,
    availablePharmacists: List<Pharmacist>,
    cameraPositionState: CameraPositionState,
    cameraMoveEvent: SharedFlow<LatLng>,

    onSearchQueryChange: (String) -> Unit,
    onSearchArea: (Double, Double) -> Unit,
    onPharmacySelect: (Pharmacy) -> Unit,
    onPharmacistSelect: (String) -> Unit,
    onMapModeChange: (Boolean) -> Unit,
    onBackFromMap: () -> Unit
) {
    if (isMapView) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = "map_view" }
        ) {
            PharmacySelector(
                pharmacies = searchResults,
                selectedPharmacy = selectedPharmacy,
                onPharmacyClick = onPharmacySelect,
                onSearch = onSearchQueryChange,
                onSearchArea = onSearchArea,
                onBackClick = onBackFromMap,
                cameraPositionState = cameraPositionState,
                cameraMoveEvent = cameraMoveEvent,
                sheetContent = if (selectedPharmacy != null) {
                    {
                        PharmacistListPanel(
                            pharmacyName = selectedPharmacy.name,
                            pharmacists = availablePharmacists,
                            onPharmacistSelect = { pharmacist ->
                                onPharmacistSelect(pharmacist.userId)
                            }
                        )
                    }
                } else null
            )
        }
    } else {
        PharmacistSearchView(
            searchText = searchQuery,
            pharmacies = searchResults,
            onSearchChange = onSearchQueryChange,
            onNavigateToMap = { onMapModeChange(true) },
            onPharmacySelect = { pharmacy ->
                onMapModeChange(true)
                onPharmacySelect(pharmacy)
            }
        )
    }
}

@ThemePreviews
@Composable
private fun ConsultPharmacistContentPreview() {
    PharmMasterTheme {
        ConsultPharmacistContent(
            isMapView = false,
            searchQuery = "",
            searchResults = emptyList(),
            selectedPharmacy = null,
            availablePharmacists = emptyList(),
            cameraPositionState = rememberCameraPositionState(),
            cameraMoveEvent = MutableSharedFlow(),
            onSearchQueryChange = {},
            onSearchArea = { _, _ -> },
            onPharmacySelect = {},
            onPharmacistSelect = {},
            onMapModeChange = {},
            onBackFromMap = {}
        )
    }
}