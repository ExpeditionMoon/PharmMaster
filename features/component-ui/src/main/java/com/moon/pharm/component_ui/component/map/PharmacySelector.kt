package com.moon.pharm.component_ui.component.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacySelector(
    pharmacies: List<Pharmacy>,
    selectedPharmacy: Pharmacy?,
    onPharmacyClick: (Pharmacy) -> Unit,
    onSearch: (String) -> Unit,
    onSearchArea: (Double, Double) -> Unit,
    onBackClick: () -> Unit,
    sheetContent: (@Composable () -> Unit)? = null,
    bottomContent: @Composable () -> Unit = {},
    cameraMoveEvent: SharedFlow<LatLng>? = null,
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL),
            15f
        )
    },
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(pharmacies, selectedPharmacy) {
        if (pharmacies.isNotEmpty() || selectedPharmacy != null) {
            scaffoldState.bottomSheetState.expand()
        }
    }

    if (cameraMoveEvent != null) {
        LaunchedEffect(Unit) {
            cameraMoveEvent.collect { latLng ->
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                )
            }
        }
    }

    LaunchedEffect(selectedPharmacy) {
        selectedPharmacy?.let { pharmacy ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(pharmacy.latitude, pharmacy.longitude),
                    16f
                )
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 80.dp,
        sheetContainerColor = White,
        sheetContent = {
            if (sheetContent != null) {
                sheetContent()
            } else {
                PharmacyListPanel(
                    pharmacies = pharmacies,
                    onPharmacyClick = onPharmacyClick
                )
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PharmacyMap(
                pharmacies = pharmacies,
                selectedPharmacy = selectedPharmacy,
                onPharmacyClick = onPharmacyClick,
                onBackClick = onBackClick,
                showBackButton = false,
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            )

            MapSearchBar(
                value = searchText,
                onValueChange = { searchText = it },
                onSearch = onSearch,
                onBackClick = onBackClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            MapRefreshButton(
                onClick = {
                    val target = cameraPositionState.position.target
                    onSearchArea(target.latitude, target.longitude)
                },
                modifier = Modifier.align(Alignment.TopCenter)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                bottomContent()
            }
        }
    }
}
