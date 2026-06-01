package com.moon.pharm.profile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.ValidateLoginFormUseCase
import com.moon.pharm.domain.usecase.user.SyncFcmTokenUseCase
import com.moon.pharm.profile.auth.model.LoginUiMessage
import com.moon.pharm.profile.auth.screen.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateLoginFormUseCase: ValidateLoginFormUseCase,
    private val syncFcmTokenUseCase: SyncFcmTokenUseCase
) : ViewModel() {

    // region 1. State
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()
    // endregion

    // region 2. User Actions
    fun updateEmail(input: String) {
        _uiState.update { it.copy(email = input) }
    }

    fun updatePassword(input: String) {
        _uiState.update { it.copy(password = input) }
    }

    fun login() {
        val currentState = _uiState.value
        val validationResult = validateLoginFormUseCase(currentState.email, currentState.password)

        if (validationResult is ValidateLoginFormUseCase.Result.Invalid) {
            val errorState = when(validationResult.error) {
                ValidateLoginFormUseCase.ErrorType.EMPTY_EMAIL -> LoginUiMessage.EmptyEmail
                ValidateLoginFormUseCase.ErrorType.EMPTY_PASSWORD -> LoginUiMessage.EmptyPassword
            }

            showMessage(errorState)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.login(currentState.email, currentState.password)
            if (result is DataResourceResult.Success) { syncFcmTokenUseCase() }
            when (result) {
                is DataResourceResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(LoginEffect.NavigateHome)
                }
                is DataResourceResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(LoginEffect.ShowMessage(LoginUiMessage.LoginFailed))
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    // endregion

    // region 3. System Actions
    private fun showMessage(message: LoginUiMessage) {
        viewModelScope.launch {
            _effect.emit(LoginEffect.ShowMessage(message))
        }
    }
    // endregion
}
