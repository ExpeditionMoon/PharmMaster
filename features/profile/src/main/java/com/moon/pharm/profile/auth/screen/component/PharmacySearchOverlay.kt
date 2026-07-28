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
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.maps.DEFAULT_SEOUL_LATITUDE
import com.moon.pharm.maps.DEFAULT_SEOUL_LONGITUDE
import com.moon.pharm.maps.component.PharmacySelector
import com.moon.pharm.maps.model.MapPlace
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.model.SignUpPharmacyUiModel
import com.moon.pharm.profile.auth.screen.SignUpUiState
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel

@Composable
fun PharmacySearchOverlay(
    uiState: SignUpUiState,
    viewModel: SignUpViewModel,
    onClose: () -> Unit
) {
    var tempSelectedPharmacy by remember { mutableStateOf<SignUpPharmacyUiModel?>(null) }
    var isLocationGranted by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(DEFAULT_SEOUL_LATITUDE, DEFAULT_SEOUL_LONGITUDE), 15f)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        isLocationGranted = isGranted
        if (isGranted) viewModel.fetchCurrentLocationAndSearch()
        else viewModel.fetchNearbyPharmacies(DEFAULT_SEOUL_LATITUDE, DEFAULT_SEOUL_LONGITUDE)
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    PharmacySelector(
        pharmacies = uiState.pharmacySearchResults.map { it.toMapPlace() },
        selectedPharmacy = tempSelectedPharmacy?.toMapPlace(),
        isLocationEnabled = isLocationGranted,
        onPharmacyClick = { selected ->
            tempSelectedPharmacy = uiState.pharmacySearchResults.find { it.placeId == selected.placeId }
        },
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

private fun SignUpPharmacyUiModel.toMapPlace(): MapPlace {
    return MapPlace(
        id = id,
        placeId = placeId,
        name = name,
        address = address,
        tel = tel,
        latitude = latitude,
        longitude = longitude
    )
}
