package com.moon.pharm.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.usecase.user.SyncFcmTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncFcmTokenUseCase: SyncFcmTokenUseCase
) : ViewModel() {

    private val _isSplashLoading = MutableStateFlow(true)
    val isSplashLoading = _isSplashLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _isSplashLoading.value = false
        }

        viewModelScope.launch {
            try {
                syncFcmTokenUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}