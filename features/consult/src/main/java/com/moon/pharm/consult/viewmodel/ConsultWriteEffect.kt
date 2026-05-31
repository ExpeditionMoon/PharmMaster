package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage

sealed interface ConsultWriteEffect {
    data class ShowMessage(val message: UiMessage) : ConsultWriteEffect
    data class MoveCamera(val lat: Double, val lng: Double) : ConsultWriteEffect
    data object MoveToPharmacist : ConsultWriteEffect
    data object CreateSuccess : ConsultWriteEffect
    data object UpdateSuccess : ConsultWriteEffect
}
