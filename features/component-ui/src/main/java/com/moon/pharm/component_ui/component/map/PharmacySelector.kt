package com.moon.pharm.component_ui.component.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.pharmacy.Pharmacy

@Composable
fun PharmacySelector(
    pharmacies: List<Pharmacy>,
    selectedPharmacy: Pharmacy?,
    onPharmacyClick: (Pharmacy) -> Unit,
    onSearch: (String) -> Unit,
    onSearchArea: (Double, Double) -> Unit,
    onBackClick: () -> Unit,
    bottomContent: @Composable () -> Unit = {},
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

    Box(modifier = modifier.fillMaxSize()) {
        PharmacyMap(
            pharmacies = pharmacies,
            selectedPharmacy = selectedPharmacy,
            onPharmacyClick = onPharmacyClick,
            onBackClick = onBackClick,
            showBackButton = false,
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White, RoundedCornerShape(10.dp)),
                placeholder = { Text("약국 이름 검색") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable { onBackClick() }
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.clickable {
                            onSearch(searchText)
                            focusManager.clearFocus()
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch(searchText)
                    focusManager.clearFocus()
                }),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedIndicatorColor = Primary,
                    unfocusedIndicatorColor = Placeholder
                ),
                shape = RoundedCornerShape(10.dp)
            )
        }

        Button(
            onClick = {
                val target = cameraPositionState.position.target
                onSearchArea(target.latitude, target.longitude)
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 80.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = Primary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "이 위치에서 재검색",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            bottomContent()
        }
    }
}