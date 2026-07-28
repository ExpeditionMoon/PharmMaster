package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.consult.mapper.toUiModel
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.designsystem.common.UiMessage
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
class ConsultListViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchConsultList()
    }

    fun onTabSelected(tab: ConsultPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun fetchConsultList() {
        viewModelScope.launch {
            val profileResult = consultUseCases.getCurrentUserConsultProfile()
            val userId = if (profileResult is DataResourceResult.Success) profileResult.resultData.userId else null
            val isPharmacist = if (profileResult is DataResourceResult.Success) {
                profileResult.resultData.isPharmacist
            } else {
                false
            }

            _uiState.update {
                it.copy(currentUserId = userId, isPharmacist = isPharmacist)
            }

            consultUseCases.getConsultList().collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            consultList = result.resultData.map { it.toUiModel() }
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
}
