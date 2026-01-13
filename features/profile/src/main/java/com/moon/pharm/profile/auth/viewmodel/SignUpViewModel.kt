package com.moon.pharm.profile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.AuthUseCases
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.screen.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface AuthEvent {
    object NavigateToMain : AuthEvent
}
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val useCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<AuthEvent>()

    init {
        if (useCases.getCurrentUserId() != null) {
            viewModelScope.launch {
                _eventFlow.emit(AuthEvent.NavigateToMain)
            }
        }
    }

    fun updateUserType(type: String) {
        _uiState.update { it.copy(userType = type) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, isEmailAvailable = null) }
    }

    fun checkEmailDuplicate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isEmailChecking = true) }

            val isDuplicated = useCases.checkEmailDuplicate(_uiState.value.email)
            _uiState.update { it.copy(isEmailAvailable = !isDuplicated, isEmailChecking = false) }
        }
    }

    fun updateNickName(name: String) {
        _uiState.update { it.copy(nickName = name) }
    }

    fun updateProfileImage(uriString: String?) {
        _uiState.update { it.copy(profileImageUri = uriString) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun moveToNextStep(onComplete: () -> Unit) {
        when (_uiState.value.currentStep) {
            SignUpStep.TYPE -> {
                _uiState.update { it.copy(currentStep = SignUpStep.EMAIL) }
            }

            SignUpStep.EMAIL -> {
                if (_uiState.value.isEmailAvailable == true && _uiState.value.password.length >= 6) {
                    _uiState.update { it.copy(currentStep = SignUpStep.NICKNAME) }
                }
            }

            SignUpStep.NICKNAME -> {
                signUpUser(onComplete)
            }
        }
    }

    private fun signUpUser(onComplete: () -> Unit) {
        val currentState = _uiState.value
        val type = when (currentState.userType) {
            "약사" -> UserType.PHARMACIST
            else -> UserType.GENERAL
        }

        val user = User(
            id = "",
            email = currentState.email,
            nickName = currentState.nickName,
            userType = type,
            profileImageUrl = currentState.profileImageUri,
            createdAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            useCases.signUp(user, currentState.password).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is DataResourceResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, isComplete = true) }
                        onComplete()
                    }

                    is DataResourceResult.Failure -> {
                        val errorMsg = result.exception.message ?: "알 수 없는 에러"
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}