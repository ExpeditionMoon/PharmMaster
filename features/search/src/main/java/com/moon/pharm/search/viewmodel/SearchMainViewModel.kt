package com.moon.pharm.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.repository.DrugSearchRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.search.model.SearchUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchMainViewModel @Inject constructor(
    private val drugSearchRepository: DrugSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

    init {
        observeSearchQuery()
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            uiState
                .map { it.searchQuery }
                .debounce(500L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                drugs = emptyList(),
                                isSearchExecuted = false
                            )
                        }
                        return@collectLatest
                    }
                    searchDrugs(query)
                }
        }
    }

    private suspend fun searchDrugs(query: String) {
        _uiState.update { it.copy(isLoading = true) }

        val result = drugSearchRepository.searchDrugByName(itemName = query)

        when (result) {
            is DataResourceResult.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        drugs = result.resultData,
                        isSearchExecuted = true
                    )
                }
            }
            is DataResourceResult.Failure -> {
                _uiState.update { it.copy(isLoading = false) }
                _effect.emit(
                    SearchEffect.ShowMessage(
                        result.exception.message?.let { msg -> SearchUiMessage.DynamicError(msg) }
                            ?: SearchUiMessage.SearchFailed
                    )
                )
            }
            DataResourceResult.Loading -> {
                _uiState.update { it.copy(isLoading = true) }
            }
        }
    }
}
