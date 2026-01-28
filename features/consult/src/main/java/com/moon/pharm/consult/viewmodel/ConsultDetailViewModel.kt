package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultDetailViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsultDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _answerContent = MutableStateFlow("")
    val answerContent = _answerContent.asStateFlow()

    fun getConsultDetail(id: String) {
        viewModelScope.launch {
            consultUseCases.getConsultDetail(id).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            selectedItem = result.resultData.consult,
                            answerPharmacist = result.resultData.pharmacist,
                            canAnswer = result.resultData.isMyConsultToAnswer
                        )
                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                            userMessage = UiMessage.LoadDataFailed
                        )
                    }
                }
            }
        }
    }

    fun onAnswerContentChanged(content: String) {
        _answerContent.value = content
    }

    fun registerAnswer(consultId: String) {
        val content = _answerContent.value
        if (content.isBlank()) return

        val pharmacist = _uiState.value.answerPharmacist
        if (pharmacist == null) {
            _uiState.update { it.copy(userMessage = UiMessage.LoadDataFailed) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            consultUseCases.registerAnswer(consultId, content, pharmacist).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> { _uiState.update { it.copy(isLoading = true) } }
                    is DataResourceResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                selectedItem = result.resultData,
                                canAnswer = false,
                                userMessage = ConsultUiMessage.AnswerRegisterSuccess
                            )
                        }
                        _answerContent.value = ""
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false, userMessage = ConsultUiMessage.CreateFailed) }
                    }
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}