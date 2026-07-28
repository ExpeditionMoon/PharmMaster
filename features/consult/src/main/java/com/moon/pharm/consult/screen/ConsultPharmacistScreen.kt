package com.moon.pharm.consult.screen

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.consult.R
import com.moon.pharm.consult.screen.component.ConsultPharmacistContent
import com.moon.pharm.consult.viewmodel.ConsultWriteViewModel
import com.moon.pharm.designsystem.component.bar.PharmTopBar
import com.moon.pharm.designsystem.model.TopBarData
import com.moon.pharm.designsystem.model.TopBarNavigationType
import com.moon.pharm.maps.DEFAULT_SEOUL_LATITUDE
import com.moon.pharm.maps.DEFAULT_SEOUL_LONGITUDE
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultPharmacistScreen(
    viewModel: ConsultWriteViewModel,
    onMapModeChanged: (Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToConfirm: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isMapView by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(DEFAULT_SEOUL_LATITUDE, DEFAULT_SEOUL_LONGITUDE),
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
            viewModel.fetchNearbyPharmacies(DEFAULT_SEOUL_LATITUDE, DEFAULT_SEOUL_LONGITUDE)
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

    val handleBackPress = {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
            scope.launch { scaffoldState.bottomSheetState.partialExpand() }
        } else if (isMapView) {
            isMapView = false
        } else {
            onNavigateUp()
        }
    }

    BackHandler { handleBackPress() }

    Scaffold(
        topBar = {
            if (!isMapView) {
                PharmTopBar(
                    data = TopBarData(
                        title = stringResource(R.string.consult_write_title),
                        navigationType = TopBarNavigationType.Back,
                        onNavigationClick = { handleBackPress() }
                    )
                )
            }
        },
        contentWindowInsets = if (isMapView) WindowInsets(0.dp) else ScaffoldDefaults.contentWindowInsets
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ConsultPharmacistContent(
                isMapView = isMapView,
                searchQuery = uiState.searchQuery,
                searchResults = uiState.searchResults,
                selectedPharmacy = uiState.selectedPharmacy,
                availablePharmacists = uiState.availablePharmacists,
                cameraPositionState = cameraPositionState,
                cameraMoveEvent = viewModel.moveCameraEvent,
                onSearchQueryChange = viewModel::onSearchQueryChanged,
                onSearchArea = viewModel::fetchNearbyPharmacies,
                onPharmacySelect = viewModel::selectPharmacy,
                onPharmacistSelect = { pharmacistId ->
                    viewModel.selectPharmacist(pharmacistId)
                    isMapView = false
                    onNavigateToConfirm()
                },
                onMapModeChange = { isMap -> isMapView = isMap },
                onBackFromMap = {
                    if (uiState.selectedPharmacy != null) {
                        viewModel.clearSelectedPharmacy()
                    } else {
                        isMapView = false
                    }
                }
            )
        }
    }
}
