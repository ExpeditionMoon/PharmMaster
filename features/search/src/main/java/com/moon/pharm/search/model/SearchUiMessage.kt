package com.moon.pharm.search.model

sealed interface SearchUiMessage {
    data object SearchFailed : SearchUiMessage
    data object EmptyQuery : SearchUiMessage
    data class DynamicError(val message: String) : SearchUiMessage
}