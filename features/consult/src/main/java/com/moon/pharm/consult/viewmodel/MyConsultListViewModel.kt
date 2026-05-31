package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyConsultListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val consultRepository: ConsultRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyConsultListUiState(isLoading = true))
    val uiState: StateFlow<MyConsultListUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MyConsultListEffect>()
    val effect = _effect.asSharedFlow()

    init {
        fetchMyConsultList()
    }

    private fun fetchMyConsultList() {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            userRepository.getUser(userId).collectLatest { userResult ->
                if (userResult !is DataResourceResult.Success) {
                    return@collectLatest
                }

                val user = userResult.resultData
                val isPharmacist = user.userType == UserType.PHARMACIST
                val currentNickname = user.nickName

                _uiState.update {
                    it.copy(currentUserId = userId, isPharmacist = isPharmacist)
                }

                val consultFlow = if (isPharmacist) {
                    consultRepository.getMyAnsweredConsultList(userId)
                } else {
                    consultRepository.getMyConsult(userId)
                }

                consultFlow.collectLatest { result ->
                    when (result) {
                        is DataResourceResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is DataResourceResult.Success -> {
                            val displayedConsults =
                                if (!isPharmacist && currentNickname.isNotEmpty()) {
                                    result.resultData.map { item ->
                                        item.copy(nickName = currentNickname)
                                    }
                                } else {
                                    result.resultData
                                }

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    myConsults = displayedConsults
                                )
                            }
                        }
                        is DataResourceResult.Failure -> {
                            val shouldShowMessage = _uiState.value.myConsults.isEmpty()
                            _uiState.update { it.copy(isLoading = false) }
                            if (shouldShowMessage) {
                                _effect.emit(MyConsultListEffect.ShowMessage(UiMessage.LoadDataFailed))
                            }
                        }
                    }
                }
            }
        }
    }
}
