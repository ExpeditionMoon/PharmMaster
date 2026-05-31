package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage

sealed interface ConsultListEffect {
    data class ShowMessage(val message: UiMessage) : ConsultListEffect
}
