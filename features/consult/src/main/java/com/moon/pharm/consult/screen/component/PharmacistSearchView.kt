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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.chip.FilterChip
import com.moon.pharm.component_ui.component.input.SearchBar
import com.moon.pharm.component_ui.component.item.PharmacyListItem
import com.moon.pharm.component_ui.theme.backgroundLight
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
                    PharmacyListItem(
                        pharmacy = pharmacy,
                        onClick = onPharmacySelect
                    )
                }
            }
        } else {
            MapFindBanner(onClick = onNavigateToMap)
        }
    }
}