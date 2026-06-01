package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.chip.FilterChip
import com.moon.pharm.component_ui.component.input.SearchBar
import com.moon.pharm.component_ui.component.item.PharmacyListItem
import com.moon.pharm.component_ui.model.PharmacyUiModel
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.pharmacy.Pharmacy

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
            .background(PharmTheme.colors.background)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.consult_search_select_pharmacist),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PharmTheme.colors.onSurface,
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
            FilterChip(text = stringResource(R.string.consult_search_nearby), isSelected = true, onClick = {})
            FilterChip(text = stringResource(R.string.consult_search_favorite), isSelected = false, onClick = {})
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (searchText.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items (pharmacies){ pharmacy ->
                    PharmacyListItem(
                        pharmacy = pharmacy.toUiModel(),
                        onClick = { onPharmacySelect(pharmacy) }
                    )
                }
            }
        } else {
            MapFindBanner(onClick = onNavigateToMap)
        }
    }
}

@ThemePreviews
@Composable
private fun PharmacistSearchViewPreview() {
    PharmMasterTheme {
        PharmacistSearchView(
            searchText = "?щ튆",
            pharmacies = listOf(
                Pharmacy(
                    id = "pharm_001",
                    placeId = "place_001",
                    name = "?щ튆?쎄뎅",
                    address = "?쒖슱?밸퀎??媛뺣궓援??뚰뿤?濡?123",
                    tel = "02-1234-5678",
                    latitude = 37.498095,
                    longitude = 127.027610
                )
            ),
            onSearchChange = {},
            onNavigateToMap = {},
            onPharmacySelect = {}
        )
    }
}

private fun Pharmacy.toUiModel(): PharmacyUiModel =
    PharmacyUiModel(
        id = id,
        placeId = placeId,
        name = name,
        address = address,
        latitude = latitude,
        longitude = longitude
    )
