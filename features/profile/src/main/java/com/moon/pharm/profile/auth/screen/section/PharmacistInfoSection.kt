package com.moon.pharm.profile.auth.screen.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.profile.R
import com.moon.pharm.component_ui.R as UiR

@Composable
fun PharmacistInfoSection(
    pharmacyName: String,
    bio: String,
    onSearchClick: () -> Unit,
    onUpdateBio: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = pharmacyName,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSearchClick() },
            enabled = false,
            readOnly = true,
            label = { Text(stringResource(R.string.signup_pharmacy_name_label)) },
            placeholder = { Text(stringResource(R.string.signup_pharmacy_name_placeholder)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(UiR.string.desc_search_icon),
                    modifier = Modifier.clickable { onSearchClick() }
                )
            },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                disabledTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                disabledLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = onUpdateBio,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            label = { Text(stringResource(R.string.signup_bio_label)) },
            placeholder = { Text(stringResource(R.string.signup_bio_placeholder)) },
            shape = RoundedCornerShape(10.dp),
            maxLines = 5
        )
    }
}