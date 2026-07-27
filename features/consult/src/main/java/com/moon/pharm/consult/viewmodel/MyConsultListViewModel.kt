package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.mapper.toUiModel
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.GetMyConsultListUseCase
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
    private val getMyConsultListUseCase: GetMyConsultListUseCase
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
        viewModelScope.launch {
            getMyConsultListUseCase().collectLatest { profileResult ->
                if (profileResult !is DataResourceResult.Success) return@collectLatest

                val profile = profileResult.resultData
                _uiState.update {
                    it.copy(currentUserId = profile.userId, isPharmacist = profile.isPharmacist)
                }

                profile.consultsFlow.collectLatest { result ->
                    _uiState.update { state ->
                        when (result) {
                            is DataResourceResult.Loading -> state.copy(isLoading = true)
                            is DataResourceResult.Success -> {
                                val displayedConsults =
                                    if (!profile.isPharmacist && profile.nickname.isNotEmpty()) {
                                        result.resultData.map { item ->
                                            item.copy(nickName = profile.nickname)
                                        }
                                    } else {
                                        result.resultData
                                    }
                                state.copy(
                                    isLoading = false,
                                    myConsults = displayedConsults.map { it.toUiModel() },
                                    userMessage = null
                                )
                            }
                            is DataResourceResult.Failure -> state.copy(
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
