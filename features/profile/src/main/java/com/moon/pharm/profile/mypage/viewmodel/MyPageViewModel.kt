package com.moon.pharm.profile.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.auth.LogoutUseCase
import com.moon.pharm.domain.usecase.consult.GetMyConsultUseCase
import com.moon.pharm.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getMyConsultUseCase: GetMyConsultUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val userId = getCurrentUserIdUseCase()
        if (userId.isNullOrEmpty()) {
            _uiState.value = MyPageUiState(isLoading = false, userMessage = UiMessage.LoginRequired)
            return
        }

        viewModelScope.launch {
            combine(
                getUserUseCase(userId),
                getMyConsultUseCase(userId)
            ) { userResult, consultResult ->
                val user = if (userResult is DataResourceResult.Success) userResult.resultData else null
                val consults = if (consultResult is DataResourceResult.Success) consultResult.resultData else emptyList()
                val isLoading = userResult is DataResourceResult.Loading || consultResult is DataResourceResult.Loading
                val errorMsg: UiMessage? = when {
                    userResult is DataResourceResult.Failure -> UiMessage.LoadDataFailed
                    consultResult is DataResourceResult.Failure -> UiMessage.LoadDataFailed
                    else -> null
                }

                MyPageUiState(
                    isLoading = isLoading,
                    user = user,
                    myConsults = consults,
                    userMessage = errorMsg
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}