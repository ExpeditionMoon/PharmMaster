package com.moon.pharm.ddi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.ddi.mapper.toUiMessage
import com.moon.pharm.ddi.mapper.toUiModel
import com.moon.pharm.ddi.model.DdiUiMessage
import com.moon.pharm.domain.model.ddi.DdiException
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.ddi.AnalyzeDdiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DdiSharedViewModel @Inject constructor(
    private val analyzeDdiUseCase: AnalyzeDdiUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DdiUiState())
    val uiState: StateFlow<DdiUiState> = _uiState.asStateFlow()

    fun addDrug(drugName: String) {
        _uiState.update { state ->
            val updatedList = (state.selectedDrugs + drugName).distinct()
            state.copy(selectedDrugs = updatedList, result = null, userMessage = null)
        }
    }

    fun removeDrug(drugName: String) {
        _uiState.update { state ->
            val updatedList = state.selectedDrugs.filter { it != drugName }
            state.copy(selectedDrugs = updatedList, result = null, userMessage = null)
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun clearAll() {
        _uiState.update { DdiUiState() }
    }

    fun analyzeInteractions() {
        val drugsToAnalyze = _uiState.value.selectedDrugs

        if (drugsToAnalyze.size < 2) {
            _uiState.update { it.copy(userMessage = DdiUiMessage.DynamicError("비교할 약물을 2개 이상 담아주세요.")) }
            return
        }

        _uiState.update { it.copy(isLoading = true, userMessage = null, result = null) }

        viewModelScope.launch {
            when (val result = analyzeDdiUseCase(drugsToAnalyze)) {
                is DataResourceResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is DataResourceResult.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, result = result.resultData.toUiModel())
                    }
                }
                is DataResourceResult.Failure -> {
                    val uiMessage = (result.exception as? DdiException)?.toUiMessage()
                        ?: DdiUiMessage.Unknown

                    _uiState.update {
                        it.copy(isLoading = false, userMessage = uiMessage)
                    }
                }
            }
        }
    }
}