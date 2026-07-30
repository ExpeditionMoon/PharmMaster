package com.moon.pharm.profile.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.LogoutUseCase
import com.moon.pharm.domain.usecase.user.ObserveCurrentUserUseCase
import com.moon.pharm.domain.usecase.user.ObserveMyPageConsultsUseCase
import com.moon.pharm.domain.usecase.user.UpdateNicknameUseCase
import com.moon.pharm.profile.mypage.mapper.toMyPageUiModel
import com.moon.pharm.profile.mypage.model.MyPageConsultStatusUiModel
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
class MyPageViewModel @Inject constructor(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val observeMyPageConsultsUseCase: ObserveMyPageConsultsUseCase,
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()
    private var consultJob: Job? = null
    private var consultOwner: Pair<String, UserType>? = null

    init {
        observeProfile()
    }

    fun updateNickname(newNickname: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isNicknameUpdating = true) }

            val result = updateNicknameUseCase(newNickname)

            _uiState.update { state ->
                when (result) {
                    is DataResourceResult.Success -> state.copy(
                        isNicknameUpdating = false,
                        user = state.user?.copy(nickName = newNickname)
                    )
                    is DataResourceResult.Failure -> state.copy(
                        isNicknameUpdating = false,
                        userMessage = result.exception.toUiMessage("닉네임 변경에 실패했습니다.")
                    )
                    DataResourceResult.Loading -> state.copy(isNicknameUpdating = false)
                }
            }
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            observeCurrentUserUseCase().collectLatest { result ->
                when (result) {
                    DataResourceResult.Loading -> _uiState.update { state ->
                        state.copy(isProfileLoading = state.user == null)
                    }
                    is DataResourceResult.Failure -> _uiState.update {
                        it.copy(
                            isProfileLoading = false,
                            userMessage = UiMessage.LoadDataFailed
                        )
                    }
                    is DataResourceResult.Success -> {
                        val user = result.resultData
                        _uiState.update {
                            it.copy(
                                isProfileLoading = false,
                                user = user.toMyPageUiModel()
                            )
                        }
                        if (consultOwner != (user.id to user.userType)) {
                            loadConsults(user.id, user.userType)
                        }
                    }
                }
            }
        }
    }

    fun retryConsults() {
        val user = _uiState.value.user ?: return
        loadConsults(
            userId = user.id,
            userType = if (user.isPharmacist) UserType.PHARMACIST else UserType.GENERAL
        )
    }

    private fun loadConsults(userId: String, userType: UserType) {
        consultOwner = userId to userType
        consultJob?.cancel()
        consultJob = viewModelScope.launch {
            observeMyPageConsultsUseCase(userId, userType).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        DataResourceResult.Loading -> state.copy(consultState = MyPageConsultState.Loading)
                        is DataResourceResult.Failure -> state.copy(consultState = MyPageConsultState.Error)
                        is DataResourceResult.Success -> {
                            val consults = result.resultData.map { it.toMyPageUiModel() }
                            state.copy(
                                consultState = MyPageConsultState.Content(
                                    consults = consults,
                                    historyText = consults.toHistoryText(state.user?.isPharmacist == true)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            when (val result = logoutUseCase()) {
                is DataResourceResult.Success -> _uiState.update { it.copy(isLogoutSuccess = true) }
                is DataResourceResult.Failure -> _uiState.update {
                    it.copy(userMessage = result.exception.toUiMessage("로그아웃에 실패했습니다."))
                }
                DataResourceResult.Loading -> Unit
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun List<com.moon.pharm.profile.mypage.model.MyPageConsultUiModel>.toHistoryText(
        isPharmacist: Boolean
    ): String? {
        if (isEmpty()) return null
        return if (isPharmacist) {
            "${count { it.status == MyPageConsultStatusUiModel.Completed }}/$size"
        } else {
            size.toString()
        }
    }

    private fun Throwable.toUiMessage(defaultMessage: String): UiMessage {
        return UiMessage.Error(message?.takeIf(String::isNotBlank) ?: defaultMessage)
    }
}
