package com.moon.pharm.search.viewmodel

import com.moon.pharm.search.model.DrugUiModel
import com.moon.pharm.search.model.SearchUiMessage

data class SearchUiState(
    val isLoading: Boolean = false,
    val userMessage: SearchUiMessage? = null,

    val drugs: List<DrugUiModel> = emptyList(),
    val isSearchExecuted: Boolean = false
)
