package com.moon.pharm.component_ui.component.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import kotlin.collections.first
import kotlin.collections.isNotEmpty

@Composable
fun PharmacyMap(
    pharmacies: List<Pharmacy>,
    selectedPharmacy: Pharmacy? = null,
    onPharmacyClick: (Pharmacy) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultLocation = LatLng(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    LaunchedEffect(selectedPharmacy, pharmacies) {
        if (selectedPharmacy != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(selectedPharmacy.latitude, selectedPharmacy.longitude), 16f
                )
            )
        } else if (pharmacies.isNotEmpty()) {
            val first = pharmacies.first()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(first.latitude, first.longitude), 14f
                )
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = true,
                mapToolbarEnabled = false
            )
        ) {
            pharmacies.forEach { pharmacy ->
                Marker(
                    state = MarkerState(position = LatLng(pharmacy.latitude, pharmacy.longitude)),
                    title = pharmacy.name,
                    snippet = pharmacy.address,
                    onClick = {
                        onPharmacyClick(pharmacy)
                        false
                    }
                )
            }
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.desc_back_button),
                tint = Color.Black
            )
        }
    }
}