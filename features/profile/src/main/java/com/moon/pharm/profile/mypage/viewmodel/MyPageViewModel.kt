package com.moon.pharm.profile.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.LogoutUseCase
import com.moon.pharm.domain.usecase.user.ObserveMyPageDataUseCase
import com.moon.pharm.domain.usecase.user.UpdateNicknameUseCase
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
        val currentUser = uiState.value.user ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = updateNicknameUseCase(currentUser, newNickname)

            if (result is DataResourceResult.Failure) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userMessage = UiMessage.LoadDataFailed
                )
            } else {
                val updatedUser = currentUser.copy(nickName = newNickname)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = updatedUser
                )
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            observeMyPageDataUseCase().collectLatest { result ->
                val currentState = _uiState.value
                val user = if (result is DataResourceResult.Success) result.resultData.user else currentState.user
                val consults = if (result is DataResourceResult.Success) result.resultData.consults else currentState.myConsults
                val isLoading = result is DataResourceResult.Loading
                val errorMsg: UiMessage? = if (result is DataResourceResult.Failure) UiMessage.LoadDataFailed else null
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
