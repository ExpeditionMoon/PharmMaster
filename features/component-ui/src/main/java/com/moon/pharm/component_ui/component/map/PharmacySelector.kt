package com.moon.pharm.component_ui.component.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.PharmacyListPreviewProvider
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.pharmacy.Pharmacy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacySelector(
    pharmacies: List<Pharmacy>,
    selectedPharmacy: Pharmacy?,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onPharmacyClick: (Pharmacy) -> Unit,
    onSearch: (String) -> Unit,
    onSearchArea: (Double, Double) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLocationEnabled: Boolean = false,
    sheetContent: (@Composable () -> Unit)? = null,
    bottomContent: @Composable () -> Unit = {},
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL),
            15f
        )
    }
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(pharmacies, selectedPharmacy) {
        if (pharmacies.isNotEmpty() || selectedPharmacy != null) {
            scaffoldState.bottomSheetState.expand()
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
        sheetContainerColor = PharmTheme.colors.surface,
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
                onPharmacyClick = onPharmacyClick,
                onBackClick = onBackClick,
                showBackButton = false,
                cameraPositionState = cameraPositionState,
                isLocationEnabled = isLocationEnabled,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusManager.clearFocus()
                    }
            )

            MapSearchBar(
                value = searchText,
                onValueChange = onSearchTextChange,
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

@ThemePreviews
@Composable
private fun PharmacySelectorPreview(
    @PreviewParameter(PharmacyListPreviewProvider::class) pharmacies: List<Pharmacy>
) {
    PharmMasterTheme {
        PharmacySelector(
            pharmacies = pharmacies,
            selectedPharmacy = null,
            searchText = "",
            onSearchTextChange = {},
            onPharmacyClick = {},
            onSearch = {},
            onSearchArea = { _, _ -> },
            onBackClick = {}
        )
    }
}
