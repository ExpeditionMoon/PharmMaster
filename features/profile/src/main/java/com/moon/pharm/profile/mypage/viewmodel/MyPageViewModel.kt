package com.moon.pharm.profile.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.LogoutUseCase
import com.moon.pharm.domain.usecase.user.ObserveMyPageDataUseCase
import com.moon.pharm.domain.usecase.user.UpdateNicknameUseCase
import com.moon.pharm.profile.mypage.mapper.toMyPageUiModel
import com.moon.pharm.profile.mypage.model.MyPageConsultStatusUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val observeMyPageDataUseCase: ObserveMyPageDataUseCase,
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun updateNickname(newNickname: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = updateNicknameUseCase(newNickname)

            if (result is DataResourceResult.Failure) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userMessage = UiMessage.LoadDataFailed
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = _uiState.value.user?.copy(nickName = newNickname)
                )
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            observeMyPageDataUseCase().collectLatest { result ->
                val currentState = _uiState.value
                val user = if (result is DataResourceResult.Success) {
                    result.resultData.user.toMyPageUiModel()
                } else {
                    currentState.user
                }
                val consults = if (result is DataResourceResult.Success) {
                    result.resultData.consults.map { it.toMyPageUiModel() }
                } else {
                    currentState.myConsults
                }
                val isLoading = result is DataResourceResult.Loading
                val errorMsg: UiMessage? = if (result is DataResourceResult.Failure) UiMessage.LoadDataFailed else null
                val isPharmacist = user?.isPharmacist == true
                val totalCount = consults.size

                val countText = if (isPharmacist) {
                    val completedCount = consults.count { it.status == MyPageConsultStatusUiModel.Completed }
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
