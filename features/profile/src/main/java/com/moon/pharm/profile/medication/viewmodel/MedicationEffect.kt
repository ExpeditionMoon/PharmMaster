package com.moon.pharm.profile.medication.viewmodel

import com.moon.pharm.component_ui.common.UiMessage

sealed interface MedicationEffect {
    data class ShowMessage(val message: UiMessage) : MedicationEffect
    data object NavigateMedicationHome : MedicationEffect
}
