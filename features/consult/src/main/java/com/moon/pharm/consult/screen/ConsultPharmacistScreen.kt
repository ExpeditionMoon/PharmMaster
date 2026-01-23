package com.moon.pharm.consult.screen

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.component.map.PharmacySelector
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.screen.component.PharmacistListPanel
import com.moon.pharm.consult.screen.component.PharmacistSearchView
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultPharmacistScreen(
    navController: NavController,
    viewModel: ConsultViewModel,
    onMapModeChanged: (Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val writeState = uiState.writeState

    val searchResults = writeState.searchResults
    val availablePharmacists = writeState.availablePharmacists

    var isMapView by remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL),
            15f
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            viewModel.fetchCurrentLocationAndPharmacies()
        } else {
            viewModel.fetchNearbyPharmacies(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)
        }
    }

    LaunchedEffect(isMapView) {
        onMapModeChanged(isMapView)
        if (isMapView) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    BackHandler {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
            scope.launch { scaffoldState.bottomSheetState.partialExpand() }
        } else if (isMapView) {
            isMapView = false
            viewModel.clearWriteState()
        } else {
            navController.popBackStack()
        }
    }

    if (isMapView) {
        PharmacySelector(
            pharmacies = writeState.searchResults,
            selectedPharmacy = writeState.selectedPharmacy,
            onPharmacyClick = { pharmacy ->
                viewModel.selectPharmacy(pharmacy)
            },
            onSearch = { query -> viewModel.onSearchQueryChanged(query) },
            onSearchArea = { lat, lng -> viewModel.fetchNearbyPharmacies(lat, lng) },
            onBackClick = {
                if (writeState.selectedPharmacy != null) {
                    viewModel.clearSelectedPharmacy()
                } else {
                    isMapView = false
                }
            },
            cameraPositionState = cameraPositionState,
            cameraMoveEvent = viewModel.moveCameraEvent,
            sheetContent = if (writeState.selectedPharmacy != null) {
                {
                    PharmacistListPanel(
                        pharmacyName = writeState.selectedPharmacy.name,
                        pharmacists = writeState.availablePharmacists,
                        onPharmacistSelect = { pharmacist ->
                            viewModel.selectPharmacist(pharmacist.userId)
                            viewModel.selectPharmacist(pharmacist.userId)
                            navController.navigate(ContentNavigationRoute.ConsultTabConfirmScreen)
                        }
                    )
                }
            } else null
        )
    } else {
        PharmacistSearchView(
            searchText = writeState.searchQuery,
            pharmacies = searchResults,
            onSearchChange = { viewModel.onSearchQueryChanged(it) },
            onNavigateToMap = { isMapView = true },
            onPharmacySelect = { pharmacy ->
                isMapView = true
                viewModel.selectPharmacy(pharmacy)
            }
        )
    }
}