package com.moon.pharm.consult.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.moon.pharm.component_ui.component.map.PharmacyMap
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.tertiaryLight
import com.moon.pharm.consult.R
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultPharmacistScreen(
    navController: NavController,
    viewModel: ConsultViewModel,
    onMapModeChanged: (Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val writeState = uiState.writeState

    val searchResults = writeState.searchResults
    val availablePharmacists = writeState.availablePharmacists

    var isMapView by remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }

        if (isGranted) {
            val hasFineLocation = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasCoarseLocation = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            @SuppressLint("MissingPermission")
            if (hasFineLocation || hasCoarseLocation) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        viewModel.fetchNearbyPharmacies(location.latitude, location.longitude)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(isMapView) {
        onMapModeChanged(isMapView)
    }

    LaunchedEffect(writeState.selectedPharmacy) {
        if (writeState.selectedPharmacy != null) {
            scaffoldState.bottomSheetState.expand()
        }
    }

    BackHandler {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
            scope.launch { scaffoldState.bottomSheetState.partialExpand() }
        } else if (isMapView) {
            isMapView = false
        } else {
            navController.popBackStack()
        }
    }

    if (isMapView) {
        PharmacistMapView(
            scaffoldState = scaffoldState,
            pharmacies = searchResults,
            selectedPharmacy = writeState.selectedPharmacy,
            pharmacists = availablePharmacists,
            pharmacyName = writeState.selectedPharmacy?.name ?: stringResource(R.string.consult_map_pharmacy_default_name),
            onBack = { isMapView = false },
            onPharmacySelect = { pharmacy ->
                viewModel.selectPharmacy(pharmacy)
            },
            onPharmacistSelect = { pharmacist ->
                viewModel.selectPharmacist(pharmacist.userId)
                navController.popBackStack()
            }
        )
    } else {
        PharmacistSearchView(
            searchText = writeState.searchQuery,
            pharmacies = searchResults,
            onSearchChange = { viewModel.onSearchQueryChanged(it) },
            onNavigateToMap = { isMapView = true },
            onPharmacySelect = { pharmacy ->
                viewModel.selectPharmacy(pharmacy)
            }
        )
    }
}

@Composable
fun PharmacyItem(
    pharmacy: Pharmacy,
    onSelect: (Pharmacy) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .clickable { onSelect(pharmacy) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = pharmacy.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pharmacy.address,
                color = SecondFont,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun PharmacistItem(
    pharmacist: Pharmacist,
    onSelect: (Pharmacist) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(tertiaryLight)
                    .border(1.dp, tertiaryLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = pharmacist.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pharmacist.bio,
                    fontSize = 13.sp,
                    color = SecondFont
                )
            }
        }
        Button(
            onClick = { onSelect(pharmacist) },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(36.dp)
                .width(64.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = stringResource(R.string.consult_map_select_btn),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PharmacistSearchView(
    searchText: String,
    pharmacies: List<Pharmacy>,
    onSearchChange: (String) -> Unit,
    onNavigateToMap: () -> Unit,
    onPharmacySelect: (Pharmacy) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.consult_search_select_pharmacist),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SearchBar(
            value = searchText,
            onValueChange = onSearchChange,
            placeholder = stringResource(R.string.consult_search_placeholder)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(text = stringResource(R.string.consult_search_nearby), isSelected = true)
            FilterChip(text = stringResource(R.string.consult_search_favorite), isSelected = false)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (searchText.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items (pharmacies){ pharmacy ->
                    PharmacyItem(
                        pharmacy = pharmacy,
                        onSelect = onPharmacySelect
                    )
                }
            }
        } else {
            MapFindBanner(onClick = onNavigateToMap)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacistMapView(
    scaffoldState: BottomSheetScaffoldState,
    pharmacies: List<Pharmacy>,
    selectedPharmacy: Pharmacy?,
    pharmacists: List<Pharmacist>,
    pharmacyName: String,
    onBack: () -> Unit,
    onPharmacySelect: (Pharmacy) -> Unit,
    onPharmacistSelect: (Pharmacist) -> Unit
) {
    val scope = rememberCoroutineScope()

    BackHandler {
        onBack()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.consult_map_pharmacist_list_format, pharmacyName),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (pharmacists.isEmpty()) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.consult_map_no_pharmacist), color = SecondFont)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(pharmacists) { pharmacist ->
                            PharmacistItem(
                                pharmacist = pharmacist,
                                onSelect = onPharmacistSelect
                            )
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetContainerColor = White,
        sheetShadowElevation = 10.dp
    ) {
        PharmacyMap(
            pharmacies = pharmacies,
            selectedPharmacy = selectedPharmacy,
            onPharmacyClick = { pharmacy ->
                onPharmacySelect(pharmacy)
            },
            onBackClick = onBack
        )
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(White, RoundedCornerShape(10.dp))
            .border(0.5.dp, tertiaryLight, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.desc_search_icon),
                tint = Placeholder,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(color = Placeholder, fontSize = 14.sp)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    singleLine = true,
                    cursorBrush = SolidColor(primaryLight),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Primary else White)
            .border(
                width = if (isSelected) 0.dp else 0.5.dp,
                color = if (isSelected) Color.Transparent else tertiaryLight,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) White else Color.Gray
        )
    }
}

@Composable
fun MapFindBanner(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFF3E5F5)
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = stringResource(R.string.desc_map_icon),
                tint = Primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.consult_map_search_placeholder),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
    }
}