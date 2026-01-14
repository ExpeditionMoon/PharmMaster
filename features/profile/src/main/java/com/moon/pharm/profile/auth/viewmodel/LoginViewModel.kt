package com.moon.pharm.profile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loginEvent = MutableSharedFlow<Boolean>()
    val loginEvent = _loginEvent.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    fun updateEmail(input: String) {
        _email.value = input
    }

    fun updatePassword(input: String) {
        _password.value = input
    }

    fun login() {
        if (_email.value.isBlank() || _password.value.isBlank()) return

        viewModelScope.launch {
            loginUseCase(_email.value, _password.value).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> _isLoading.value = true
                    is DataResourceResult.Success -> {
                        _isLoading.value = false
                        _loginEvent.emit(true)
                    }
                    is DataResourceResult.Failure -> {
                        _isLoading.value = false
                        _errorMessage.emit(result.exception.message ?: "로그인 실패")
                    }
                }
            }
        }
    }
}