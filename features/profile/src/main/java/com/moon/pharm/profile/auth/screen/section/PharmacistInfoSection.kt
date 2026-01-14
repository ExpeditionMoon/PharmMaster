package com.moon.pharm.profile.auth.screen.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.profile.R

@Composable
fun PharmacistInfoSection(
    pharmacyName: String,
    bio: String,
    onUpdateName: (String) -> Unit,
    onUpdateBio: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = pharmacyName,
            onValueChange = onUpdateName,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.signup_pharmacy_name_label)) },
            placeholder = { Text(stringResource(R.string.signup_pharmacy_name_placeholder)) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = onUpdateBio,
            modifier = Modifier.fillMaxWidth().height(120.dp),
            label = { Text(stringResource(R.string.signup_bio_label)) },
            placeholder = { Text(stringResource(R.string.signup_bio_placeholder)) },
            shape = RoundedCornerShape(10.dp),
            maxLines = 5
        )
    }
}