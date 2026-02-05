package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import com.moon.pharm.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultListViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserUseCase: GetUserUseCase
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
            val userId = getCurrentUserIdUseCase()
            var isPharmacist = false

            if (userId != null) {
                val userResult = getUserUseCase(userId).first()
                if (userResult is DataResourceResult.Success) {
                    isPharmacist = userResult.resultData.userType == UserType.PHARMACIST
                }
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
                            consultList = result.resultData
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