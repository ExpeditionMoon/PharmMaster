package com.moon.pharm.consult.model

import com.moon.pharm.component_ui.common.UiMessage

sealed interface ConsultUiMessage : UiMessage {
    data object InputRequired : ConsultUiMessage
    data object TitleTooShort : ConsultUiMessage
    data object PharmacistRequired : ConsultUiMessage
    data object LoginRequired : ConsultUiMessage
    data object CreateFailed : ConsultUiMessage
    data object AnswerRegisterSuccess : ConsultUiMessage
}