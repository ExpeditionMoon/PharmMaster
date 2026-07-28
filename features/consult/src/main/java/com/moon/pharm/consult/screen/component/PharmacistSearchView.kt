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
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.toMapPlace
import com.moon.pharm.consult.model.PharmacyUiModel
import com.moon.pharm.designsystem.component.chip.FilterChip
import com.moon.pharm.designsystem.component.input.SearchBar
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.maps.component.PharmacyListItem

@Composable
fun PharmacistSearchView(
    searchText: String,
    pharmacies: List<PharmacyUiModel>,
    onSearchChange: (String) -> Unit,
    onNavigateToMap: () -> Unit,
    onPharmacySelect: (PharmacyUiModel) -> Unit
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
                        pharmacy = pharmacy.toMapPlace(),
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
            searchText = "달빛",
            pharmacies = listOf(
                PharmacyUiModel(
                    id = "pharmacy-1",
                    placeId = "place-1",
                    name = "Moon Pharmacy",
                    address = "Seoul",
                    tel = "02-0000-0000",
                    latitude = 37.5665,
                    longitude = 126.9780
                )
            ),
            onSearchChange = {},
            onNavigateToMap = {},
            onPharmacySelect = {}
        )
    }
}
