package com.moon.pharm.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.MedicationScheduleItem
import com.moon.pharm.domain.usecase.medication.ObserveTodayMedicationItemsUseCase
import com.moon.pharm.domain.usecase.medication.ObserveWeeklyMedicationAdherenceUseCase
import com.moon.pharm.domain.usecase.user.ObserveCurrentUserNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeCurrentUserNicknameUseCase: ObserveCurrentUserNicknameUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val observeTodayMedicationItemsUseCase: ObserveTodayMedicationItemsUseCase,
    private val observeWeeklyMedicationAdherenceUseCase: ObserveWeeklyMedicationAdherenceUseCase
) : ViewModel() {

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserNickname()
        observeMedicationSummary()
    }

    private fun loadUserNickname() {
        viewModelScope.launch {
            observeCurrentUserNicknameUseCase().collectLatest { result ->
                if (result is DataResourceResult.Success) {
                    _nickname.value = result.resultData
                }
            }
        }
    }

    private fun observeMedicationSummary() {
        val userId = getCurrentUserIdUseCase() ?: return
        val today = LocalDate.now()

        viewModelScope.launch {
            observeTodayMedicationItemsUseCase(userId, today).collectLatest { result ->
                if (result is DataResourceResult.Success) {
                    _uiState.update { state ->
                        state.withTodayMedicationItems(result.resultData)
                    }
                }
            }
        }

        viewModelScope.launch {
            observeWeeklyMedicationAdherenceUseCase(userId, today).collectLatest { result ->
                if (result is DataResourceResult.Success) {
                    _uiState.update { state ->
                        state.copy(
                            weeklyMedicationCount = result.resultData.totalCount,
                            weeklyCompletedCount = result.resultData.completedCount
                        )
                    }
                }
            }
        }
    }

    private fun HomeUiState.withTodayMedicationItems(items: List<MedicationScheduleItem>): HomeUiState {
        val activeItems = items.filterNot(MedicationScheduleItem::isPaused)
        val pendingGroups = activeItems
            .filter { item -> item.isAlarmEnabled && !item.isTaken }
            .groupBy { item -> "${item.intakeGroupId ?: item.medicationId}:${item.time}" }
            .values
            .map { group -> group.sortedBy(MedicationScheduleItem::name) }
            .filter { group -> group.firstOrNull()?.time.toLocalTimeOrNull()?.let { it >= LocalTime.now() } == true }
            .sortedBy { group -> group.first().time }

        val nextGroup = pendingGroups.firstOrNull()
        return copy(
            nextMedicationReminder = nextGroup?.let { group ->
                HomeMedicationReminder(
                    time = group.first().time,
                    representativeName = group.first().name,
                    medicationCount = group.size
                )
            },
            remainingReminderCount = pendingGroups.size,
            todayMedicationCount = activeItems.size,
            todayCompletedCount = activeItems.count(MedicationScheduleItem::isTaken)
        )
    }

    private fun String?.toLocalTimeOrNull(): LocalTime? = runCatching {
        this?.let(LocalTime::parse)
    }.getOrNull()
}
