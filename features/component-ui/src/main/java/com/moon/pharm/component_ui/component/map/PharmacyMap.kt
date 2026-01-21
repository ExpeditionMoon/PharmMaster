package com.moon.pharm.component_ui.component.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.pharmacy.Pharmacy

@SuppressLint("UnrememberedMutableState")
@Composable
fun PharmacyMap(
    pharmacies: List<Pharmacy>,
    selectedPharmacy: Pharmacy? = null,
    onPharmacyClick: (Pharmacy) -> Unit,
    onBackClick: () -> Unit,
    showBackButton: Boolean = true,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
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
                key(pharmacy.placeId) {
                    Marker(
                        state = MarkerState(position = LatLng(pharmacy.latitude, pharmacy.longitude)),
                        title = pharmacy.name,
                        snippet = pharmacy.address,
                        onClick = {
                            onPharmacyClick(pharmacy)
                            true
                        }
                    )
                }
            }
        }

        if (showBackButton) {
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .statusBarsPadding()
                    .align(Alignment.TopStart),
                colors = ButtonDefaults.buttonColors(containerColor = White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.desc_back_button),
                    tint = Black
                )
            }
        }
    }
}