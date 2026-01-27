package com.moon.pharm.profile.medication.model

import com.moon.pharm.component_ui.common.UiMessage

sealed interface MedicationUiMessage : UiMessage {
    object EmptyMedicationName : MedicationUiMessage
    object CreateFailed : MedicationUiMessage
    object NotLoggedIn : MedicationUiMessage
}