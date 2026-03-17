package com.moon.pharm.component_ui.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.PharmacyListPreviewProvider
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.pharmacy.Pharmacy

@Composable
fun PharmacyListItem(
    pharmacy: Pharmacy,
    onClick: (Pharmacy) -> Unit,
    modifier: Modifier = Modifier
) {
    PharmListItem(
        modifier = modifier,
        onClick = { onClick(pharmacy) },
        leading = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = PharmTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
        },
        headline = pharmacy.name,
        subhead = pharmacy.address
    )
}

@ThemePreviews
@Composable
private fun PharmacyListItemPreview(
    @PreviewParameter(PharmacyListPreviewProvider::class) pharmacies: List<Pharmacy>
) {
    PharmMasterTheme {
        Box(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp)
        ) {
            PharmacyListItem(
                pharmacy = pharmacies.first(),
                onClick = {}
            )
        }
    }
}