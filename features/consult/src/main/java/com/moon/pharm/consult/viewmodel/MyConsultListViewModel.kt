package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.consult.mapper.toUiModel
import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.GetMyConsultListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private var fetchJob: Job? = null

    init {
        fetchMyConsultList()
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun retry() {
        fetchMyConsultList()
    }

    private fun fetchMyConsultList() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            getMyConsultListUseCase().collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        DataResourceResult.Loading -> state.copy(
                            isLoading = true,
                            hasLoadError = false
                        )
                        is DataResourceResult.Success -> {
                            val profile = result.resultData
                            val displayedConsults =
                                if (!profile.isPharmacist && profile.nickname.isNotEmpty()) {
                                    profile.consults.map { item -> item.copy(nickName = profile.nickname) }
                                } else {
                                    profile.consults
                                }
                            state.copy(
                                isLoading = false,
                                hasLoadError = false,
                                myConsults = displayedConsults.map { it.toUiModel() },
                                currentUserId = profile.userId,
                                isPharmacist = profile.isPharmacist,
                                userMessage = null
                            )
                        }
                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                            hasLoadError = true,
                            userMessage = UiMessage.LoadDataFailed
                        )
                    }
                }
            }
        }
    }
}
