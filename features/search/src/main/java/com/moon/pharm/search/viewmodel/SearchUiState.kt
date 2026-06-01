package com.moon.pharm.search.viewmodel

import com.moon.pharm.domain.model.drug.Drug

data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val drugs: List<Drug> = emptyList(),
    val isSearchExecuted: Boolean = false
)
