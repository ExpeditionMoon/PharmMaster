package com.moon.pharm.profile.medication.model

import com.moon.pharm.designsystem.common.UiMessage

sealed interface MedicationUiMessage : UiMessage {
    object EmptyMedicationName : MedicationUiMessage
    object CreateFailed : MedicationUiMessage
    object NotLoggedIn : MedicationUiMessage
}