package com.moon.pharm.profile.auth.viewmodel

import com.moon.pharm.domain.model.AuthError
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.screen.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUserType(type: String) {
        _uiState.update { it.copy(userType = type) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, isEmailAvailable = null) }
    }

    fun checkEmailDuplicate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isEmailChecking = true) }

            val isDuplicated = authRepository.isEmailDuplicated(_uiState.value.email)
            _uiState.update { it.copy(isEmailAvailable = !isDuplicated, isEmailChecking = false) }
        }
    }

    fun updateNickName(name: String) {
        _uiState.update { it.copy(nickName = name) }
    }

    fun updateProfileImage(uriString: String?) {
        _uiState.update { it.copy(profileImageUri = uriString) }
    }

    fun moveToNextStep(onComplete: () -> Unit) {
        when (_uiState.value.currentStep) {
            SignUpStep.TYPE -> {
                _uiState.update { it.copy(currentStep = SignUpStep.EMAIL) }
            }

            SignUpStep.EMAIL -> {
                if (_uiState.value.isEmailAvailable == true) {
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
        val user = User(
            id = "",
            email = currentState.email,
            nickName = currentState.nickName,
            userType = currentState.userType,
            profileImageUrl = currentState.profileImageUri,
            createdAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            authRepository.signUp(user).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is DataResourceResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, isComplete = true) }
                        onComplete()
                    }

                    is DataResourceResult.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception as? AuthError
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}