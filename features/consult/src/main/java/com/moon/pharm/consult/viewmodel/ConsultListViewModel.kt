package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultListViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultListUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ConsultListEffect>()
    val effect = _effect.asSharedFlow()

    init {
        fetchConsultList()
    }

    fun onTabSelected(tab: ConsultPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun fetchConsultList() {
        viewModelScope.launch {
            val userId = consultUseCases.authRepository.getCurrentUserId()
            var isPharmacist = false

            if (userId != null) {
                val userResult = userRepository.getUserOnce(userId)
                if (userResult is DataResourceResult.Success) {
                    isPharmacist = userResult.resultData.userType == UserType.PHARMACIST
                }
            }

            _uiState.update {
                it.copy(currentUserId = userId, isPharmacist = isPharmacist)
            }

            consultUseCases.getConsultList().collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is DataResourceResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                consultList = result.resultData
                            )
                        }
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.emit(ConsultListEffect.ShowMessage(UiMessage.LoadDataFailed))
                    }
                }
            }
        }
    }
}
