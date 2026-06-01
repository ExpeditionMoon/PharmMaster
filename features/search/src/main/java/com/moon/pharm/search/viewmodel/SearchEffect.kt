package com.moon.pharm.search.viewmodel

import com.moon.pharm.search.model.SearchUiMessage

sealed interface SearchEffect {
    data class ShowMessage(val message: SearchUiMessage) : SearchEffect
}
