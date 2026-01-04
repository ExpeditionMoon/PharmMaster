package com.moon.pharm.profile.medication.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.profile.R

@Composable
fun MedicationUiMessage.asString(): String {
    return when (this) {
        MedicationUiMessage.EmptyMedicationName ->
            stringResource(R.string.error_empty_medication_name)
        MedicationUiMessage.CreateFailed ->
            stringResource(R.string.error_create_medication)
    }
}