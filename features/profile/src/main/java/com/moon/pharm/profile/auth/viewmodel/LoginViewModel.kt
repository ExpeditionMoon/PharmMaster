package com.moon.pharm.profile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.LoginUseCase
import com.moon.pharm.domain.usecase.auth.ValidateLoginFormUseCase
import com.moon.pharm.profile.auth.common.LoginUiMessage
import com.moon.pharm.profile.auth.screen.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateLoginFormUseCase: ValidateLoginFormUseCase
) : ViewModel() {

    // region 1. State
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
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

            _uiState.update { it.copy(userMessage = errorState) }
            return
        }

        viewModelScope.launch {
            loginUseCase(currentState.email, currentState.password).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            userMessage = null
                        )
                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                            userMessage = LoginUiMessage.LoginFailed
                        )
                    }
                }
            }
        }
    }
    // endregion

    // region 3. System Actions
    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
    // endregion
}