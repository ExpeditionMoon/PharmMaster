package com.moon.pharm.consult.common

import androidx.annotation.StringRes
import com.moon.pharm.component_ui.common.UiMessage

sealed interface ConsultUiMessage : UiMessage {
    data object CreateFailed : ConsultUiMessage

    data class StringResourceError(@param:StringRes val messageId: Int) : ConsultUiMessage
}
