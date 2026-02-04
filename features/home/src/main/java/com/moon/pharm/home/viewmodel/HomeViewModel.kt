package com.moon.pharm.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.user.GetNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getNicknameUseCase: GetNicknameUseCase
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
                val fetchedNickname = getNicknameUseCase.getNickname(userId)

                if (fetchedNickname.isNotEmpty()) {
                    _nickname.value = fetchedNickname
                }
            }
        }
    }
}