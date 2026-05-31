package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage

sealed interface ConsultDetailEffect {
    data class ShowMessage(val message: UiMessage) : ConsultDetailEffect
    data object NavigateBack : ConsultDetailEffect
}
