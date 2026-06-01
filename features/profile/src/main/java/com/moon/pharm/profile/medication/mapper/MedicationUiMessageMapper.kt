package com.moon.pharm.profile.medication.mapper

import android.content.Context
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase.MedicationValidatorError
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import com.moon.pharm.component_ui.R as ComponentUiR

fun UiMessage.asMedicationString(context: Context): String {
    return when (this) {
        MedicationUiMessage.EmptyMedicationName -> context.getString(R.string.error_empty_medication_name)
        MedicationUiMessage.CreateFailed -> context.getString(R.string.error_create_medication)
        MedicationUiMessage.NotLoggedIn -> context.getString(ComponentUiR.string.error_login_required)
        UiMessage.LoadDataFailed -> context.getString(ComponentUiR.string.error_load_data)
        UiMessage.LoginRequired -> context.getString(ComponentUiR.string.error_login_required)
        is UiMessage.Error -> message
        else -> context.getString(ComponentUiR.string.error_unknown)
    }
}

fun MedicationValidatorError.toUiMessage(): MedicationUiMessage {
    return when (this) {
        MedicationValidatorError.EMPTY_NAME -> MedicationUiMessage.EmptyMedicationName
    }
}
