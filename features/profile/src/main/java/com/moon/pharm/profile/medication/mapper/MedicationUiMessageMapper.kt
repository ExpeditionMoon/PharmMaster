package com.moon.pharm.profile.medication.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase.MedicationValidatorError
import com.moon.pharm.profile.R
import com.moon.pharm.component_ui.R as UiR
import com.moon.pharm.profile.medication.model.MedicationUiMessage

@Composable
fun MedicationUiMessage.asString(): String {
    return when (this) {
        MedicationUiMessage.EmptyMedicationName -> stringResource(R.string.error_empty_medication_name)
        MedicationUiMessage.CreateFailed -> stringResource(R.string.error_create_medication)
        MedicationUiMessage.NotLoggedIn -> stringResource(UiR.string.error_login_required)
    }
}

fun MedicationValidatorError.toUiMessage(): MedicationUiMessage {
    return when (this) {
        MedicationValidatorError.EMPTY_NAME -> MedicationUiMessage.EmptyMedicationName
    }
}