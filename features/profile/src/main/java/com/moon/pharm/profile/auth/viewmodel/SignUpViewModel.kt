package com.moon.pharm.profile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.CheckEmailDuplicateUseCase
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.auth.SignUpUseCase
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
    private val signUpUseCase: SignUpUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<AuthEvent>()
    val eventFlow = _eventFlow

    init {
        if (getCurrentUserIdUseCase() != null) {
            viewModelScope.launch {
                _eventFlow.emit(AuthEvent.NavigateToMain)
            }
        }
    }

    fun updateUserType(type: UserType) {
        _uiState.update { it.copy(userType = type) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, isEmailAvailable = null) }
    }

    fun checkEmailDuplicate() {
        val email = _uiState.value.email
        if (email.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isEmailChecking = true) }

            val isDuplicated = checkEmailDuplicateUseCase(email)
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

    fun updatePharmacyName(name: String) {
        _uiState.update { it.copy(pharmacyName = name) }
    }

    fun updatePharmacistBio(bio: String) {
        _uiState.update { it.copy(pharmacistBio = bio) }
    }

    fun moveToNextStep(onComplete: () -> Unit) {
        when (_uiState.value.currentStep) {
            SignUpStep.TYPE -> {
                if (_uiState.value.userType != null) {
                    _uiState.update { it.copy(currentStep = SignUpStep.EMAIL) }
                }
            }

            SignUpStep.EMAIL -> {
                if (_uiState.value.isEmailAvailable == true && _uiState.value.password.length >= 6) {
                    _uiState.update { it.copy(currentStep = SignUpStep.NICKNAME) }
                }
            }

            SignUpStep.NICKNAME -> {
                if (_uiState.value.userType == UserType.PHARMACIST) {
                    _uiState.update { it.copy(currentStep = SignUpStep.PHARMACIST_INFO) }
                } else {
                    signUpUser(onComplete)
                }
            }
            SignUpStep.PHARMACIST_INFO -> {
                signUpUser(onComplete)
            }
        }
    }

    private fun signUpUser(onComplete: () -> Unit) {
        val currentState = _uiState.value
        val type = currentState.userType ?: UserType.GENERAL

        val user = User(
            id = "",
            email = currentState.email,
            nickName = currentState.nickName,
            userType = type,
            profileImageUrl = currentState.profileImageUri,
            createdAt = System.currentTimeMillis()
        )

        val pharmacist = if (type == UserType.PHARMACIST) {
            Pharmacist(
                userId = "",
                name = currentState.nickName,
                bio = currentState.pharmacistBio,
                pharmacyId = "temp_pharmacy_id",
                pharmacyName = currentState.pharmacyName,
                isApproved = false
            )
        } else null

        viewModelScope.launch {
            signUpUseCase(user, currentState.password, pharmacist).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is DataResourceResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, isComplete = true) }
                        onComplete()
                    }

                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }
}