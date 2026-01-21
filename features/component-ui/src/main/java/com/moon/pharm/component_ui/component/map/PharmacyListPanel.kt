package com.moon.pharm.component_ui.component.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.item.PharmacyListItem
import com.moon.pharm.domain.model.pharmacy.Pharmacy

@Composable
fun PharmacyListPanel(
    pharmacies: List<Pharmacy>,
    onPharmacyClick: (Pharmacy) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "검색 결과 ${pharmacies.size}건",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        if (pharmacies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("검색된 약국이 없습니다.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pharmacies) { pharmacy ->
                    PharmacyListItem(
                        pharmacy = pharmacy,
                        onClick = {
                            onPharmacyClick(pharmacy)
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}