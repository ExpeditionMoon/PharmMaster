package com.moon.pharm.component_ui.common

interface UiMessage {
    object LoadDataFailed : UiMessage
    object LoginRequired : UiMessage
    data class Error(val message: String) : UiMessage
}