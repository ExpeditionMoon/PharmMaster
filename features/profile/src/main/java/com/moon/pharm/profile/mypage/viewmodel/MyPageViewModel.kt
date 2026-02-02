package com.moon.pharm.profile.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.auth.LogoutUseCase
import com.moon.pharm.domain.usecase.consult.GetMyAnsweredConsultUseCase
import com.moon.pharm.domain.usecase.consult.GetMyConsultUseCase
import com.moon.pharm.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getMyConsultUseCase: GetMyConsultUseCase,
    private val getMyAnsweredConsultUseCase: GetMyAnsweredConsultUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadData() {
        val userId = getCurrentUserIdUseCase()
        if (userId.isNullOrEmpty()) {
            _uiState.value = MyPageUiState(isLoading = false, userMessage = UiMessage.LoginRequired)
            return
        }

        viewModelScope.launch {
            getUserUseCase(userId).flatMapLatest { userResult ->
                val user = if (userResult is DataResourceResult.Success) userResult.resultData else null
                val isPharmacist = user?.userType == UserType.PHARMACIST

                val consultFlow = if (isPharmacist) {
                    getMyAnsweredConsultUseCase(userId)
                } else {
                    getMyConsultUseCase(userId)
                }

                consultFlow.map { consultResult ->
                    Pair(userResult, consultResult)
                }
            }.collectLatest { (userResult, consultResult) ->
                val user = if (userResult is DataResourceResult.Success) userResult.resultData else _uiState.value.user
                val consults = if (consultResult is DataResourceResult.Success) {
                    consultResult.resultData
                } else {
                    _uiState.value.myConsults
                }
                val isLoading = userResult is DataResourceResult.Loading || consultResult is DataResourceResult.Loading
                val errorMsg: UiMessage? = when {
                    userResult is DataResourceResult.Failure -> UiMessage.LoadDataFailed
                    consultResult is DataResourceResult.Failure -> UiMessage.LoadDataFailed
                    else -> null
                }
                val isPharmacist = user?.userType == UserType.PHARMACIST
                val totalCount = consults.size

                val countText = if (isPharmacist) {
                    val completedCount = consults.count { it.status == ConsultStatus.COMPLETED }
                    if (totalCount > 0) "$completedCount/$totalCount" else null
                } else {
                    if (totalCount > 0) "$totalCount" else null
                }

                _uiState.value = MyPageUiState(
                    isLoading = isLoading,
                    user = user,
                    myConsults = consults,
                    userMessage = errorMsg,
                    consultHistoryText = countText
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}