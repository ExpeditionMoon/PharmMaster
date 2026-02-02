package com.moon.pharm.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.usecase.user.SyncFcmTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncFcmTokenUseCase: SyncFcmTokenUseCase
) : ViewModel() {

    private val _isSplashLoading = MutableStateFlow(true)
    val isSplashLoading = _isSplashLoading.asStateFlow()

    private val _navigationEvent = Channel<String>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

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

    fun refreshFcmToken() {
        viewModelScope.launch {
            try {
                syncFcmTokenUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun moveToMedicationTab() {
        viewModelScope.launch {
            _navigationEvent.send("MedicationScreen")
        }
    }
}