package com.moon.pharm.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.repository.DrugSearchRepository
import com.moon.pharm.search.model.SearchUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchMainViewModel @Inject constructor(
    private val drugSearchRepository: DrugSearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.debounce(500L).distinctUntilChanged().collectLatest { query ->
                    if (query.isBlank()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false, drugs = emptyList(), isSearchExecuted = false
                            )
                        }
                        return@collectLatest
                    }
                    searchDrugs(query)
                }
        }
    }

    private suspend fun searchDrugs(query: String) {
        _uiState.update { it.copy(isLoading = true, userMessage = null) }

        val result = drugSearchRepository.searchDrugByName(itemName = query)

        result.onSuccess { drugs ->
            _uiState.update {
                it.copy(
                    isLoading = false, drugs = drugs, isSearchExecuted = true
                )
            }
        }.onFailure { error ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    userMessage = error.message?.let { msg -> SearchUiMessage.DynamicError(msg) }
                        ?: SearchUiMessage.SearchFailed)
            }
        }
    }
}