package com.moon.pharm.profile.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            _uiState.value = MyPageUiState(isLoading = false, error = "로그인이 필요합니다.")
            return
        }

        viewModelScope.launch {
            // [핵심] 유저 정보 Flow와 상담 내역 Flow를 동시에 구독하여 하나로 합침
            combine(
                getUserUseCase(userId),
                getMyConsultUseCase(userId)
            ) { userResult, consultResult ->

                val user = if (userResult is DataResourceResult.Success) userResult.resultData else null
                val consults = if (consultResult is DataResourceResult.Success) consultResult.resultData else emptyList()

                // 둘 중 하나라도 로딩 중이면 로딩 상태
                val isLoading = userResult is DataResourceResult.Loading || consultResult is DataResourceResult.Loading

                // 에러 메시지 추출
                val errorMsg = when {
                    userResult is DataResourceResult.Failure -> userResult.exception.message
                    consultResult is DataResourceResult.Failure -> consultResult.exception.message
                    else -> null
                }

                MyPageUiState(
                    isLoading = isLoading,
                    user = user,
                    myConsults = consults,
                    error = errorMsg
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            // 로그아웃 후 처리는 UI(Compose)의 LaunchedEffect에서 로그인 화면 이동 등으로 처리
        }
    }
}