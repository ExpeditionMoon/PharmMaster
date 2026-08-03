package com.moon.pharm.profile.medication.model

import com.moon.pharm.designsystem.common.UiMessage

sealed interface MedicationUiMessage : UiMessage {
    object EmptyMedicationName : MedicationUiMessage
    object CreateFailed : MedicationUiMessage
    object UpdateFailed : MedicationUiMessage
    object StatusUpdateFailed : MedicationUiMessage
    object LoadFailed : MedicationUiMessage
    object MedicationNotFound : MedicationUiMessage
    object MedicationPaused : MedicationUiMessage
    object IntakeUpdateFailed : MedicationUiMessage
    object DeleteFailed : MedicationUiMessage
    object NotLoggedIn : MedicationUiMessage
}
