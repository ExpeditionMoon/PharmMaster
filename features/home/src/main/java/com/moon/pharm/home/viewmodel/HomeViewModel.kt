package com.moon.pharm.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    init {
        loadUserNickname()
    }

    private fun loadUserNickname() {
        viewModelScope.launch {
            val userId = getCurrentUserIdUseCase()

            if (userId != null) {
                userRepository.getUser(userId).collectLatest { result ->
                    if (result is DataResourceResult.Success) {
                        _nickname.value = result.resultData.nickName
                    }
                }
            }
        }
    }
}