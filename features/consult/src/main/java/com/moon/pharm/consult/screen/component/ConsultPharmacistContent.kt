package com.moon.pharm.consult.screen.component

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.component.map.PharmacySelector
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.mapper.toComponentUiModel
import com.moon.pharm.consult.model.PharmacistUiModel
import com.moon.pharm.consult.model.PharmacyUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ConsultPharmacistContent(
    isMapView: Boolean,
    searchQuery: String,
    searchResults: List<PharmacyUiModel>,
    selectedPharmacy: PharmacyUiModel?,
    availablePharmacists: List<PharmacistUiModel>,
    cameraPositionState: CameraPositionState,
    cameraMoveEvent: SharedFlow<LatLng>,

    onSearchQueryChange: (String) -> Unit,
    onSearchArea: (Double, Double) -> Unit,
    onPharmacySelect: (PharmacyUiModel) -> Unit,
    onPharmacistSelect: (String) -> Unit,
    onMapModeChange: (Boolean) -> Unit,
    onBackFromMap: () -> Unit
) {
    if (isMapView) {
        PharmacySelector(
            pharmacies = searchResults.map { it.toComponentUiModel() },
            selectedPharmacy = selectedPharmacy?.toComponentUiModel(),
            onPharmacyClick = { selected ->
                searchResults.find { it.placeId == selected.placeId }?.let(onPharmacySelect)
            },
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
