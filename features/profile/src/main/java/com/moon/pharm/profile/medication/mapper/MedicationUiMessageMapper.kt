package com.moon.pharm.profile.medication.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase.MedicationValidatorError
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import com.moon.pharm.designsystem.R as UiR

@Composable
fun MedicationUiMessage.asString(): String {
    return when (this) {
        MedicationUiMessage.EmptyMedicationName -> stringResource(R.string.error_empty_medication_name)
        MedicationUiMessage.CreateFailed -> stringResource(R.string.error_create_medication)
        MedicationUiMessage.UpdateFailed -> stringResource(R.string.error_update_medication)
        MedicationUiMessage.StatusUpdateFailed -> stringResource(R.string.error_update_medication_status)
        MedicationUiMessage.LoadFailed -> stringResource(R.string.error_load_medication)
        MedicationUiMessage.MedicationNotFound -> stringResource(R.string.error_medication_not_found)
        MedicationUiMessage.MedicationPaused -> stringResource(R.string.error_medication_paused)
        MedicationUiMessage.IntakeUpdateFailed -> stringResource(R.string.error_update_medication_intake)
        MedicationUiMessage.DeleteFailed -> stringResource(R.string.error_delete_medication)
        MedicationUiMessage.NotLoggedIn -> stringResource(UiR.string.error_login_required)
    }
}

fun MedicationValidatorError.toUiMessage(): MedicationUiMessage {
    return when (this) {
        MedicationValidatorError.EMPTY_NAME -> MedicationUiMessage.EmptyMedicationName
    }
}
