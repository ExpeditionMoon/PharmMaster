package com.moon.pharm.profile.auth.screen.component

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.component.map.PharmacySelector
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.screen.SignUpUiState
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel

@Composable
fun PharmacySearchOverlay(
    uiState: SignUpUiState,
    viewModel: SignUpViewModel,
    onClose: () -> Unit
) {
    var tempSelectedPharmacy by remember { mutableStateOf<Pharmacy?>(null) }
    var isLocationGranted by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL), 15f)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        isLocationGranted = isGranted
        if (isGranted) viewModel.fetchCurrentLocationAndSearch()
        else viewModel.fetchNearbyPharmacies(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    PharmacySelector(
        pharmacies = uiState.pharmacySearchResults,
        selectedPharmacy = tempSelectedPharmacy,
        isLocationEnabled = isLocationGranted,
        onPharmacyClick = { tempSelectedPharmacy = it },
        cameraPositionState = cameraPositionState,
        cameraMoveEvent = viewModel.moveCameraEvent,
        onSearch = { query -> viewModel.searchPharmacies(query) },
        onSearchArea = { lat, lng -> viewModel.fetchNearbyPharmacies(lat, lng) },
        onBackClick = {
            viewModel.clearSearchResults()
            onClose()
        },
        bottomContent = {
            if (tempSelectedPharmacy != null) {
                PharmPrimaryButton(
                    text = stringResource(R.string.signup_pharmacy_selected_format, tempSelectedPharmacy?.name.orEmpty()),
                    onClick = {
                        viewModel.updatePharmacy(tempSelectedPharmacy!!)
                        viewModel.clearSearchResults()
                        onClose()
                    },
                    modifier = Modifier.padding(16.dp).navigationBarsPadding()
                )
            }
        }
    )

    BackHandler {
        viewModel.clearSearchResults()
        onClose()
    }
}