package com.moon.pharm.search.viewmodel

import com.moon.pharm.domain.model.drug.Drug
import com.moon.pharm.search.model.SearchUiMessage

data class SearchUiState(
    val isLoading: Boolean = false,
    val userMessage: SearchUiMessage? = null,

    val drugs: List<Drug> = emptyList(),
    val isSearchExecuted: Boolean = false
)