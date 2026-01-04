package com.moon.pharm.consult.common

import com.moon.pharm.component_ui.common.UiMessage

sealed interface ConsultUiMessage : UiMessage {
    object CreateFailed : ConsultUiMessage
}
