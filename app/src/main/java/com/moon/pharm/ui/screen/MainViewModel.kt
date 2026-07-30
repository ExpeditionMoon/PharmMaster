package com.moon.pharm.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.RestoreMedicationAlarmsUseCase
import com.moon.pharm.domain.usecase.user.SyncFcmTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncFcmTokenUseCase: SyncFcmTokenUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val restoreMedicationAlarmsUseCase: RestoreMedicationAlarmsUseCase
) : ViewModel() {

    private val _isSplashLoading = MutableStateFlow(true)
    val isSplashLoading = _isSplashLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<AppStartDestination?>(null)
    val startDestination = _startDestination.asStateFlow()

    private val _navigationEvent = Channel<MainNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private var restoreAlarmsJob: Job? = null

    init {
        checkLoginStatus()
        syncToken()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val userId = getCurrentUserIdUseCase()

            if (userId != null) {
                _startDestination.value = AppStartDestination.Main
            } else {
                _startDestination.value = AppStartDestination.Login
            }
            delay(500)
            _isSplashLoading.value = false
        }
    }

    private fun syncToken() {
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
            _navigationEvent.send(MainNavigationEvent.NavigateToMedication)
        }
    }

    fun restoreMedicationAlarms() {
        val userId = getCurrentUserIdUseCase() ?: return
        restoreAlarmsJob?.cancel()
        restoreAlarmsJob = viewModelScope.launch {
            restoreMedicationAlarmsUseCase(userId)
        }
    }
}

enum class AppStartDestination { Main, Login }

sealed interface MainNavigationEvent {
    data object NavigateToMedication : MainNavigationEvent
}
