package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.consult.GetMyConsultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyConsultListViewModel @Inject constructor(
    private val getMyConsultUseCase: GetMyConsultUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyConsultListUiState(isLoading = true))
    val uiState: StateFlow<MyConsultListUiState> = _uiState.asStateFlow()

    init {
        fetchMyConsultList()
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun fetchMyConsultList() {
        val userId = getCurrentUserIdUseCase()
        if (userId == null) {
            _uiState.update {
                it.copy(isLoading = false, userMessage = UiMessage.LoadDataFailed)
            }
            return
        }

        viewModelScope.launch {
            getMyConsultUseCase(userId).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> {
                            state.copy(isLoading = true)
                        }
                        is DataResourceResult.Success -> {
                            state.copy(
                                isLoading = false,
                                myConsults = result.resultData,
                                userMessage = null
                            )
                        }
                        is DataResourceResult.Failure -> {
                            state.copy(
                                isLoading = false,
                                userMessage = if (state.myConsults.isEmpty()) UiMessage.LoadDataFailed else null
                            )
                        }
                    }
                }
            }
        }
    }
}