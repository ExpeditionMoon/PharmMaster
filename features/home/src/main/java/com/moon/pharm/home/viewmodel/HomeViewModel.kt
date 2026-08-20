package com.moon.pharm.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationsUseCase
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
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeCurrentUserNicknameUseCase: ObserveCurrentUserNicknameUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getMedicationsUseCase: GetMedicationsUseCase,
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
        val userId = getCurrentUserIdUseCase() ?: run {
            _uiState.update { it.copy(isMedicationDataLoadFailed = true) }
            return
        }
        val today = LocalDate.now()

        viewModelScope.launch {
            getMedicationsUseCase(userId).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state
                        is DataResourceResult.Success -> state.copy(
                            medicationRegistrationStatus = result.resultData
                                .toHomeMedicationRegistrationStatus(today)
                        )
                        is DataResourceResult.Failure -> state.copy(
                            isMedicationDataLoadFailed = true
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            observeTodayMedicationItemsUseCase(userId, today).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state
                        is DataResourceResult.Success -> state.withTodayMedicationItems(result.resultData)
                        is DataResourceResult.Failure -> state.copy(
                            isMedicationDataLoadFailed = true
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            observeWeeklyMedicationAdherenceUseCase(userId, today).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state
                        is DataResourceResult.Success -> state.copy(
                            weeklyMedicationCount = result.resultData.totalCount,
                            weeklyCompletedCount = result.resultData.completedCount
                        )
                        is DataResourceResult.Failure -> state.copy(
                            isMedicationDataLoadFailed = true
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
            todayCompletedCount = activeItems.count(MedicationScheduleItem::isTaken),
            lastCompletedTime = activeItems
                .filter(MedicationScheduleItem::isTaken)
                .mapNotNull(MedicationScheduleItem::takenTime)
                .maxOrNull(),
            isTodayMedicationLoaded = true
        )
    }

    private fun List<Medication>.toHomeMedicationRegistrationStatus(
        today: LocalDate
    ): HomeMedicationRegistrationStatus {
        return when {
            isEmpty() -> HomeMedicationRegistrationStatus.NoMedication
            any { medication -> medication.isOngoingOn(today) } -> HomeMedicationRegistrationStatus.Ongoing
            any { medication -> medication.isScheduledToStartLater(today) } -> {
                HomeMedicationRegistrationStatus.StartsLater
            }
            any { medication -> medication.status == MedicationStatus.PAUSED } -> {
                HomeMedicationRegistrationStatus.Paused
            }
            else -> HomeMedicationRegistrationStatus.NoOngoingMedication
        }
    }

    private fun Medication.isOngoingOn(today: LocalDate): Boolean {
        if (status == MedicationStatus.PAUSED) return false
        if (status == MedicationStatus.ENDED && endDate == null) return false

        val start = startDate?.toLocalDate() ?: LocalDate.MIN
        val end = endDate?.toLocalDate()
        return !today.isBefore(start) && (end == null || !today.isAfter(end))
    }

    private fun Medication.isScheduledToStartLater(today: LocalDate): Boolean {
        return status == MedicationStatus.ACTIVE &&
            startDate?.toLocalDate()?.isAfter(today) == true
    }

    private fun Long.toLocalDate(): LocalDate {
        return Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private fun String?.toLocalTimeOrNull(): LocalTime? = runCatching {
        this?.let(LocalTime::parse)
    }.getOrNull()
}
