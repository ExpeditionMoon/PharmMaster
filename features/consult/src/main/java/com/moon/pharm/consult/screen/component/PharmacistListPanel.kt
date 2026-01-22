package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.item.PharmacistListItem
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.auth.Pharmacist

@Composable
fun PharmacistListPanel(
    pharmacyName: String,
    pharmacists: List<Pharmacist>,
    onPharmacistSelect: (Pharmacist) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        Text(
            text = stringResource(R.string.consult_map_pharmacist_list_format, pharmacyName),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (pharmacists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.consult_map_no_pharmacist), color = SecondFont)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(pharmacists) { pharmacist ->
                    PharmacistListItem(
                        pharmacist = pharmacist,
                        onSelect = onPharmacistSelect
                    )
                }
            }
        }
    }
}
