package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.consult.GetMyAnsweredConsultUseCase
import com.moon.pharm.domain.usecase.consult.GetMyConsultUseCase
import com.moon.pharm.domain.usecase.user.GetUserUseCase
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
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getMyAnsweredConsultUseCase: GetMyAnsweredConsultUseCase,
    private val getUserUseCase: GetUserUseCase
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
            getUserUseCase(userId).collectLatest { userResult ->
                var isPharmacist = false
                var currentNickname = ""

                if (userResult is DataResourceResult.Success) {
                    isPharmacist = userResult.resultData.userType == UserType.PHARMACIST
                    currentNickname = userResult.resultData.nickName
                }

                _uiState.update {
                    it.copy(currentUserId = userId, isPharmacist = isPharmacist)
                }

                val consultFlow = if (isPharmacist) {
                    getMyAnsweredConsultUseCase(userId)
                } else {
                    getMyConsultUseCase(userId)
                }

                consultFlow.collectLatest { result ->
                    _uiState.update { state ->
                        when (result) {
                            is DataResourceResult.Loading -> state.copy(isLoading = true)
                            is DataResourceResult.Success -> {
                                val displayedConsults =
                                    if (!isPharmacist && currentNickname.isNotEmpty()) {
                                        result.resultData.map { item ->
                                            item.copy(nickName = currentNickname)
                                        }
                                    } else {
                                        result.resultData
                                    }
                                state.copy(
                                    isLoading = false,
                                    myConsults = displayedConsults,
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