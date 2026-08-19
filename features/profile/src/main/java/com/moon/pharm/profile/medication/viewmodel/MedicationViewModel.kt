package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.moon.pharm.domain.model.medication.MealInterval
import com.moon.pharm.domain.model.medication.MealSlot
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationScheduleBasis
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.ChangeMedicationStatusCommand
import com.moon.pharm.domain.usecase.medication.ChangeMedicationStatusUseCase
import com.moon.pharm.domain.usecase.medication.CompleteMedicationGroupCommand
import com.moon.pharm.domain.usecase.medication.CompleteMedicationGroupUseCase
import com.moon.pharm.domain.usecase.medication.DeleteMedicationCommand
import com.moon.pharm.domain.usecase.medication.DeleteMedicationUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationsUseCase
import com.moon.pharm.domain.usecase.medication.MedicationGroupIntakeItem
import com.moon.pharm.domain.usecase.medication.MedicationStatusInput
import com.moon.pharm.domain.usecase.medication.ObserveTodayMedicationItemsUseCase
import com.moon.pharm.domain.usecase.medication.ObserveWeeklyMedicationAdherenceUseCase
import com.moon.pharm.domain.usecase.medication.SaveMedicationUseCase
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCheckUseCase
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase
import com.moon.pharm.profile.medication.mapper.MedicationUiMapper
import com.moon.pharm.profile.medication.mapper.toUiMessage
import com.moon.pharm.profile.medication.model.MealIntervalUiModel
import com.moon.pharm.profile.medication.model.MealSlotUiModel
import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationIntakeGroupOptionUiModel
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.model.MedicationScheduleBasisUiModel
import com.moon.pharm.profile.medication.model.MedicationTimeGroupUiModel
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel
import com.moon.pharm.profile.navigation.MedicationCreateRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeTodayMedicationItemsUseCase: ObserveTodayMedicationItemsUseCase,
    private val observeWeeklyMedicationAdherenceUseCase: ObserveWeeklyMedicationAdherenceUseCase,
    private val saveMedicationUseCase: SaveMedicationUseCase,
    private val changeMedicationStatusUseCase: ChangeMedicationStatusUseCase,
    private val completeMedicationGroupUseCase: CompleteMedicationGroupUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val toggleIntakeCheckUseCase: ToggleIntakeCheckUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val validateMedicationEntryUseCase: ValidateMedicationEntryUseCase,
) : ViewModel() {

    // region 1. State & Derived State
    private val _uiState = MutableStateFlow(MedicationUiState())
    val uiState = _uiState.asStateFlow()

    private var isSaving = false

    val groupedMedications: StateFlow<List<MedicationTimeGroupUiModel>> = uiState
        .map { state ->
            state.medicationList
                .filter { state.selectedTab.includes(it.type) }
                .groupBy { item -> "${item.intakeGroupId ?: item.medicationId}:${item.time}" }
                .map { (id, items) ->
                    MedicationTimeGroupUiModel(
                        id = id,
                        time = items.firstOrNull()?.time,
                        items = items.sortedBy(TodayMedicationUiModel::name)
                    )
                }
                .sortedBy { it.time }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // endregion

    init {
        fetchMedicationList()
        initializeFormFromArgs()
    }

    // region 2. Event Handler
    fun onEvent(event: MedicationUiEvent) {
        when (event) {
            is MedicationUiEvent.UpdateType -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedType = event.type)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedType = event.type) }
                }
            }
            is MedicationUiEvent.UpdateName -> updateForm(event.index) { it.copy(medicationName = event.name) }
            is MedicationUiEvent.UpdateDosage -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            if (it.usesIndividualDosage) it else it.copy(medicationDosage = event.dosage)
                        }
                        state.copy(
                            sharedMedicationDosage = event.dosage,
                            medicationForms = newForms
                        )
                    }
                } else {
                    _uiState.update { state ->
                        val isMultipleMedication = state.medicationForms.size > 1
                        val updatedForms = state.medicationForms.mapIndexed { index, form ->
                            if (index == event.index) {
                                form.copy(
                                    medicationDosage = event.dosage,
                                    usesIndividualDosage = isMultipleMedication || form.usesIndividualDosage
                                )
                            } else {
                                form
                            }
                        }
                        state.copy(medicationForms = updatedForms)
                    }
                }
            }
            is MedicationUiEvent.ToggleIndividualDosage -> {
                _uiState.update { state ->
                    val updatedForms = state.medicationForms.mapIndexed { index, form ->
                        if (index != event.index) {
                            form
                        } else if (form.usesIndividualDosage) {
                            form.copy(
                                medicationDosage = state.sharedMedicationDosage,
                                usesIndividualDosage = false
                            )
                        } else {
                            form.copy(usesIndividualDosage = true)
                        }
                    }
                    state.copy(medicationForms = updatedForms)
                }
            }
            MedicationUiEvent.ToggleIndividualDosageEditor -> {
                _uiState.update { state ->
                    state.copy(
                        isIndividualDosageEditorVisible = !state.isIndividualDosageEditorVisible
                    )
                }
            }
            is MedicationUiEvent.UpdateStartDate -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map { it.copy(startDate = event.millis) }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(startDate = event.millis) }
                }
            }

            is MedicationUiEvent.UpdateEndDate -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map { it.copy(endDate = event.millis) }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(endDate = event.millis) }
                }
            }
            is MedicationUiEvent.UpdatePeriod -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val updatedForms = state.medicationForms.map { form ->
                            form.copy(startDate = event.start, endDate = event.end, noEndDate = event.noEnd)
                        }
                        state.copy(medicationForms = updatedForms)
                    }
                } else {
                    updateForm(event.index) {
                        it.copy(startDate = event.start, endDate = event.end, noEndDate = event.noEnd)
                    }
                }
            }
            is MedicationUiEvent.UpdateMealTiming -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedMealTiming = event.timing, intakeGroupId = null)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedMealTiming = event.timing, intakeGroupId = null) }
                }
            }
            is MedicationUiEvent.UpdateScheduleBasis -> {
                val update: (MedicationFormState) -> MedicationFormState = { form ->
                    form.copy(
                        scheduleBasis = event.basis,
                        selectedMealTiming = event.basis.toMealTiming(),
                        selectedMealSlots = if (event.basis.isMealBased()) form.selectedMealSlots else emptySet(),
                        intakeGroupId = null,
                        isAlarmEnabled = if (event.basis == MedicationScheduleBasisUiModel.AsNeeded) false else form.isAlarmEnabled
                    )
                }
                if (event.index == -1) {
                    _uiState.update { state -> state.copy(medicationForms = state.medicationForms.map(update)) }
                } else {
                    updateForm(event.index, update)
                }
            }
            is MedicationUiEvent.ToggleMealSlot -> {
                val update: (MedicationFormState) -> MedicationFormState = { form ->
                    form.copy(
                        selectedMealSlots = form.selectedMealSlots.toMutableSet().apply {
                            if (!add(event.slot)) remove(event.slot)
                        },
                        intakeGroupId = null
                    )
                }
                if (event.index == -1) {
                    _uiState.update { state -> state.copy(medicationForms = state.medicationForms.map(update)) }
                } else {
                    updateForm(event.index, update)
                }
            }
            is MedicationUiEvent.UpdateMealInterval -> {
                val update: (MedicationFormState) -> MedicationFormState = {
                    it.copy(mealInterval = event.interval, intakeGroupId = null)
                }
                if (event.index == -1) {
                    _uiState.update { state -> state.copy(medicationForms = state.medicationForms.map(update)) }
                } else {
                    updateForm(event.index, update)
                }
            }

            is MedicationUiEvent.UpdateAlarmTime -> {
                val newTime = (event.hour * 60 + event.minute).toLong()

                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedTime = newTime, intakeGroupId = null)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedTime = newTime, intakeGroupId = null) }
                }
            }
            is MedicationUiEvent.UpdateRepeatType -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedRepeatType = event.type, intakeGroupId = null)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedRepeatType = event.type, intakeGroupId = null) }
                }
            }
            is MedicationUiEvent.UpdateGroupedNotification -> {
                _uiState.update { state ->
                    val update: (MedicationFormState) -> MedicationFormState = { form ->
                        form.copy(
                            isGrouped = event.enabled,
                            intakeGroupId = when {
                                !event.enabled -> null
                                form.intakeGroupId != null -> form.intakeGroupId
                                else -> state.existingIntakeGroups.firstOrNull { group -> form.matches(group) }?.id
                            }
                        )
                    }
                    val forms = if (event.index == -1) {
                        state.medicationForms.map(update)
                    } else {
                        state.medicationForms.mapIndexed { index, form ->
                            if (index == event.index) update(form) else form
                        }
                    }
                    state.copy(medicationForms = forms)
                }
            }
            is MedicationUiEvent.JoinExistingIntakeGroup -> {
                val update: (MedicationFormState) -> MedicationFormState = { form ->
                    form.copy(
                        intakeGroupId = event.group.id,
                        isGrouped = true,
                        selectedTime = event.group.time,
                        selectedMealTiming = event.group.mealTiming,
                        scheduleBasis = event.group.scheduleBasis,
                        selectedMealSlots = event.group.mealSlots,
                        mealInterval = event.group.mealInterval,
                        selectedRepeatType = event.group.repeatType,
                        selectedWeeklyDays = event.group.weeklyDays
                    )
                }
                if (event.index == -1) {
                    _uiState.update { state -> state.copy(medicationForms = state.medicationForms.map(update)) }
                } else {
                    updateForm(event.index, update)
                }
            }
            is MedicationUiEvent.ToggleWeeklyDay -> {
                val update: (MedicationFormState) -> MedicationFormState = { form ->
                    form.copy(
                        selectedWeeklyDays = form.selectedWeeklyDays.toMutableSet().apply {
                            if (!add(event.day)) remove(event.day)
                        },
                        intakeGroupId = null
                    )
                }
                if (event.index == -1) {
                    _uiState.update { state -> state.copy(medicationForms = state.medicationForms.map(update)) }
                } else {
                    updateForm(event.index, update)
                }
            }
            is MedicationUiEvent.UpdateAlarmEnabled -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        state.copy(medicationForms = state.medicationForms.map {
                            it.copy(isAlarmEnabled = event.enabled)
                        })
                    }
                } else {
                    updateForm(event.index) { it.copy(isAlarmEnabled = event.enabled) }
                }
            }
            MedicationUiEvent.AddMedication -> {
                _uiState.update { state ->
                    val sharedForm = state.medicationForms.firstOrNull() ?: MedicationFormState()
                    val sharedDosage = state.sharedMedicationDosage.ifBlank {
                        sharedForm.medicationDosage.orEmpty()
                    }
                    val newForm = sharedForm.copy(
                        medicationId = null,
                        intakeGroupId = null,
                        scheduleId = null,
                        medicationName = "",
                        medicationDosage = sharedDosage,
                        usesIndividualDosage = false
                    )
                    state.copy(
                        sharedMedicationDosage = sharedDosage,
                        medicationForms = state.medicationForms + newForm
                    )
                }
            }
            is MedicationUiEvent.RemoveMedication -> {
                _uiState.update { state ->
                    val newForms = state.medicationForms.toMutableList().apply {
                        if (event.index in indices) { removeAt(event.index) }
                    }
                    if (newForms.isEmpty()) { newForms.add(MedicationFormState()) }
                    state.copy(medicationForms = newForms)
                }
            }

            // 2. 주요 비즈니스 로직
            MedicationUiEvent.SaveAllMedications -> saveAllMedications()
            is MedicationUiEvent.PauseMedication -> changeMedicationStatus(
                medicationId = event.medicationId,
                status = MedicationStatusInput.PAUSED
            )
            is MedicationUiEvent.ResumeMedication -> changeMedicationStatus(
                medicationId = event.medicationId,
                status = MedicationStatusInput.ACTIVE
            )
            is MedicationUiEvent.EndMedication -> changeMedicationStatus(
                medicationId = event.medicationId,
                status = MedicationStatusInput.ENDED
            )
            is MedicationUiEvent.DeleteMedication -> deleteMedication(event.medicationId)
            is MedicationUiEvent.ToggleTaken -> toggleMedicationTaken(
                medicationId = event.medicationId,
                scheduleId = event.scheduleId
            )
            is MedicationUiEvent.CompleteGroup -> completeMedicationGroup(event.items)

            // 3. UI 상태 및 시스템 이벤트
            is MedicationUiEvent.SelectTab -> _uiState.update { it.copy(selectedTab = event.tab) }
            MedicationUiEvent.MessageShown -> _uiState.update { it.copy(userMessage = null) }
        }
    }

    fun onTabSelected(tab: MedicationPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
    // endregion

    // region 3. Actions
    private fun fetchMedicationList() {
        val userId = getCurrentUserIdUseCase() ?: return
        val today = LocalDate.now()

        viewModelScope.launch {
            observeTodayMedicationItemsUseCase(userId, today).collectLatest { result ->
                if (isSaving) return@collectLatest

                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> currentState.copy(
                            isLoading = true,
                            userMessage = null
                        )
                        is DataResourceResult.Success -> {
                            currentState.copy(
                                isLoading = false,
                                medicationList = MedicationUiMapper.toUiModelList(result.resultData)
                            )
                        }
                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = MedicationUiMessage.LoadFailed
                            )
                        }
                    }
                }
            }
        }

        observeWeeklyAdherence(userId, today)
        observeExistingIntakeGroups(userId)
    }

    private fun observeExistingIntakeGroups(userId: String) {
        viewModelScope.launch {
            getMedicationsUseCase(userId).collectLatest { result ->
                if (result is DataResourceResult.Success) {
                    _uiState.update { state ->
                        state.copy(
                            existingIntakeGroups = result.resultData
                                .filter { it.intakeGroupId != null }
                                .groupBy { it.intakeGroupId.orEmpty() }
                                .mapNotNull { (id, medications) -> medications.firstOrNull()?.toGroupOption(id) }
                                .sortedBy(MedicationIntakeGroupOptionUiModel::representativeName)
                        )
                    }
                }
            }
        }
    }

    private fun observeWeeklyAdherence(userId: String, today: LocalDate) {
        viewModelScope.launch {
            observeWeeklyMedicationAdherenceUseCase(userId, today).collectLatest { result ->
                if (isSaving) return@collectLatest

                if (result is DataResourceResult.Success) {
                    _uiState.update {
                        it.copy(
                            weeklyTotalCount = result.resultData.totalCount,
                            weeklyCompletedCount = result.resultData.completedCount
                        )
                    }
                }
            }
        }

    }

    private fun initializeFormFromArgs() {
        val route = runCatching {
            savedStateHandle.toRoute<MedicationCreateRoute>()
        }.getOrNull()

        val medicationId = route?.medicationId
        if (medicationId != null) {
            loadMedicationForEdit(medicationId)
            return
        }

        val newForms = route
            ?.scannedMedicationNames
            ?.takeIf { it.isNotEmpty() }
            ?.map {
                MedicationFormState(
                    medicationName = it,
                    dailyCount = 1
                )
            }
            ?: listOf(MedicationFormState())

        _uiState.update {
            it.copy(
                medicationForms = newForms,
                isPrescriptionReview = route?.isPrescriptionReview == true,
                sharedMedicationDosage = newForms.firstOrNull()?.medicationDosage.orEmpty()
            )
        }
    }

    private fun loadMedicationForEdit(medicationId: String) {
        val userId = getCurrentUserIdUseCase()
        if (userId == null) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.NotLoggedIn) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getMedicationsUseCase(userId)
                .filter { it !is DataResourceResult.Loading }
                .first()) {
                is DataResourceResult.Success -> {
                    val medication = result.resultData.find { it.id == medicationId }
                    _uiState.update {
                        if (medication == null) {
                            it.copy(isLoading = false, userMessage = MedicationUiMessage.MedicationNotFound)
                        } else {
                            it.copy(
                                isLoading = false,
                                isEditing = true,
                                medicationForms = listOf(medication.toFormState())
                            )
                        }
                    }
                }
                is DataResourceResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, userMessage = MedicationUiMessage.LoadFailed) }
                }
                DataResourceResult.Loading -> Unit
            }
        }
    }

    private fun saveAllMedications() {
        if (_uiState.value.isLoading) return
        val forms = _uiState.value.medicationForms

        val invalidForm = forms.find { form ->
            validateMedicationEntryUseCase(form.medicationName) is ValidateMedicationEntryUseCase.Result.Error
        }

        if (invalidForm != null) {
            val result = validateMedicationEntryUseCase(invalidForm.medicationName) as ValidateMedicationEntryUseCase.Result.Error
            _uiState.update { it.copy(userMessage = result.error.toUiMessage()) }
            return
        }

        val existingGroups = _uiState.value.existingIntakeGroups.associateBy { it.id }
        val hasMismatchedGroupSchedule = forms.any { form ->
            form.intakeGroupId
                ?.let(existingGroups::get)
                ?.let { group -> !form.matches(group) }
                ?: false
        }
        if (hasMismatchedGroupSchedule) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.IntakeGroupScheduleMismatch) }
            return
        }

        val userId = getCurrentUserIdUseCase()
        if (userId == null) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.NotLoggedIn) }
            return
        }

        viewModelScope.launch {
            isSaving = true
            _uiState.update { it.copy(isLoading = true) }
            var isAllSuccess = true
            val intakeGroupId = forms
                .takeIf { it.size > 1 && it.all(MedicationFormState::isGrouped) }
                ?.firstOrNull()
                ?.intakeGroupId
                ?: if (forms.size > 1 && forms.all(MedicationFormState::isGrouped)) UUID.randomUUID().toString() else null

            forms.forEach { form ->
                val command = MedicationUiMapper.toSaveCommand(
                    form = form,
                    userId = userId,
                    intakeGroupId = if (form.isGrouped) intakeGroupId ?: form.intakeGroupId else null
                )

                val result = saveMedicationUseCase(command)
                    .filter { it !is DataResourceResult.Loading }
                    .first()

                if (result !is DataResourceResult.Success) {
                    isAllSuccess = false
                }
            }

            _uiState.update { currentState ->
                isSaving = false
                if (isAllSuccess) {
                    currentState.copy(
                        isLoading = false,
                        isMedicationCreated = true
                    )
                } else {
                    currentState.copy(
                        isLoading = false,
                        userMessage = if (currentState.isEditing) {
                            MedicationUiMessage.UpdateFailed
                        } else {
                            MedicationUiMessage.CreateFailed
                        }
                    )
                }
            }
        }
    }

    fun toggleMedicationTaken(medicationId: String, scheduleId: String) {
        val userId = getCurrentUserIdUseCase() ?: return

        val currentList = _uiState.value.medicationList
        val targetItem = currentList.find {
            it.medicationId == medicationId && it.scheduleId == scheduleId
        } ?: return

        if (targetItem.isPaused) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.MedicationPaused) }
            return
        }

        val newIsTaken = !targetItem.isTaken
        _uiState.update { state ->
            val newList = state.medicationList.map { item ->
                if (item.medicationId == medicationId && item.scheduleId == scheduleId) {
                    item.copy(isTaken = newIsTaken)
                } else {
                    item
                }
            }
            state.copy(medicationList = newList)
        }

        viewModelScope.launch {
            val command = MedicationUiMapper.toToggleCommand(
                uiModel = targetItem,
                userId = userId,
                isTaken = newIsTaken
            )

            toggleIntakeCheckUseCase(command).collectLatest { result ->
                if (result is DataResourceResult.Failure) {
                    _uiState.update { state ->
                        state.copy(
                            medicationList = state.medicationList.map { item ->
                                if (item.medicationId == medicationId && item.scheduleId == scheduleId) {
                                    item.copy(isTaken = targetItem.isTaken)
                                } else {
                                    item
                                }
                            },
                            userMessage = MedicationUiMessage.IntakeUpdateFailed
                        )
                    }
                }
            }
        }
    }

    private fun changeMedicationStatus(medicationId: String, status: MedicationStatusInput) {
        val userId = getCurrentUserIdUseCase()
        if (userId == null) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.NotLoggedIn) }
            return
        }

        viewModelScope.launch {
            isSaving = true
            _uiState.update { it.copy(isLoading = true) }
            val result = changeMedicationStatusUseCase(
                ChangeMedicationStatusCommand(
                    userId = userId,
                    medicationId = medicationId,
                    status = status,
                    changedAt = System.currentTimeMillis()
                )
            ).filter { it !is DataResourceResult.Loading }.first()

            isSaving = false
            _uiState.update {
                it.copy(
                    isLoading = false,
                    medicationList = if (result is DataResourceResult.Success) {
                        it.medicationList.updateMedicationStatus(medicationId, status)
                    } else {
                        it.medicationList
                    },
                    userMessage = (result as? DataResourceResult.Failure)
                        ?.let { MedicationUiMessage.StatusUpdateFailed }
                )
            }
        }
    }

    private fun deleteMedication(medicationId: String) {
        val userId = getCurrentUserIdUseCase()
        if (userId == null) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.NotLoggedIn) }
            return
        }

        viewModelScope.launch {
            isSaving = true
            _uiState.update { it.copy(isLoading = true) }
            val result = deleteMedicationUseCase(
                DeleteMedicationCommand(userId = userId, medicationId = medicationId)
            )
                .filter { it !is DataResourceResult.Loading }
                .first()

            isSaving = false
            _uiState.update {
                it.copy(
                    isLoading = false,
                    medicationList = if (result is DataResourceResult.Success) {
                        it.medicationList.filterNot { item -> item.medicationId == medicationId }
                    } else {
                        it.medicationList
                    },
                    userMessage = (result as? DataResourceResult.Failure)
                        ?.let { MedicationUiMessage.DeleteFailed }
                )
            }
        }

    }
    // endregion

    // region 4. Helper Functions
    private fun updateForm(index: Int, block: (MedicationFormState) -> MedicationFormState) {
        _uiState.update { state ->
            val updatedForms = state.medicationForms.toMutableList()
            if (index in updatedForms.indices) {
                updatedForms[index] = block(updatedForms[index])
            }
            state.copy(medicationForms = updatedForms)
        }
    }

    private fun Medication.toFormState(): MedicationFormState {
        val schedule = schedules.firstOrNull()
        return MedicationFormState(
            medicationId = id,
            intakeGroupId = intakeGroupId,
            scheduleId = schedule?.id,
            medicationName = name,
            medicationDosage = schedule?.dosage.orEmpty(),
            selectedType = type.toUiModel(),
            startDate = startDate,
            endDate = endDate,
            noEndDate = endDate == null,
            selectedMealTiming = schedule?.mealTiming?.toUiModel() ?: MealTimingUiModel.BeforeMeal,
            scheduleBasis = schedule?.basis?.toUiModel() ?: MedicationScheduleBasisUiModel.FixedTime,
            selectedMealSlots = schedules.mapNotNull { it.mealSlot?.toUiModel() }.toSet(),
            mealInterval = schedule?.mealInterval?.toUiModel() ?: MealIntervalUiModel.Immediately,
            selectedTime = schedule?.time?.toMinuteOfDay(),
            selectedRepeatType = repeatType.toUiModel(),
            selectedWeeklyDays = weeklyDays,
            isGrouped = isGrouped,
            isAlarmEnabled = isAlarmEnabled,
            status = status.toInput()
        )
    }

    private fun Medication.toGroupOption(groupId: String): MedicationIntakeGroupOptionUiModel? {
        val primarySchedule = schedules.firstOrNull() ?: return null
        return MedicationIntakeGroupOptionUiModel(
            id = groupId,
            representativeName = name,
            time = primarySchedule.time.toMinuteOfDay(),
            mealTiming = primarySchedule.mealTiming.toUiModel(),
            scheduleBasis = primarySchedule.basis.toUiModel(),
            mealSlots = schedules.mapNotNull { it.mealSlot?.toUiModel() }.toSet(),
            mealInterval = primarySchedule.mealInterval.toUiModel(),
            repeatType = repeatType.toUiModel(),
            weeklyDays = weeklyDays
        )
    }

    private fun MedicationFormState.matches(group: MedicationIntakeGroupOptionUiModel): Boolean =
        selectedTime == group.time &&
            selectedMealTiming == group.mealTiming &&
            scheduleBasis == group.scheduleBasis &&
            selectedMealSlots == group.mealSlots &&
            mealInterval == group.mealInterval &&
            selectedRepeatType == group.repeatType &&
            selectedWeeklyDays == group.weeklyDays

    private fun MedicationType.toUiModel(): MedicationTypeUiModel = when (this) {
        MedicationType.PRESCRIPTION -> MedicationTypeUiModel.Prescription
        MedicationType.OTC -> MedicationTypeUiModel.Otc
        MedicationType.SUPPLEMENT -> MedicationTypeUiModel.Supplement
    }

    private fun MealTiming.toUiModel(): MealTimingUiModel = when (this) {
        MealTiming.BEFORE_MEAL -> MealTimingUiModel.BeforeMeal
        MealTiming.DURING_MEAL -> MealTimingUiModel.DuringMeal
        MealTiming.AFTER_MEAL -> MealTimingUiModel.AfterMeal
        MealTiming.NONE -> MealTimingUiModel.None
    }

    private fun MedicationScheduleBasisUiModel.toMealTiming(): MealTimingUiModel = when (this) {
        MedicationScheduleBasisUiModel.BeforeMeal -> MealTimingUiModel.BeforeMeal
        MedicationScheduleBasisUiModel.AfterMeal -> MealTimingUiModel.AfterMeal
        MedicationScheduleBasisUiModel.FixedTime,
        MedicationScheduleBasisUiModel.AsNeeded -> MealTimingUiModel.None
    }

    private fun MedicationScheduleBasisUiModel.isMealBased(): Boolean =
        this == MedicationScheduleBasisUiModel.BeforeMeal || this == MedicationScheduleBasisUiModel.AfterMeal

    private fun completeMedicationGroup(items: List<MedicationGroupIntakeItem>) {
        val userId = getCurrentUserIdUseCase() ?: return
        val untakenItems = items.filter { item ->
            _uiState.value.medicationList.any {
                it.medicationId == item.medicationId &&
                    it.scheduleId == item.scheduleId &&
                    !it.isTaken &&
                    !it.isPaused
            }
        }
        if (untakenItems.isEmpty()) return

        viewModelScope.launch {
            val result = completeMedicationGroupUseCase(
                CompleteMedicationGroupCommand(
                    userId = userId,
                    items = untakenItems,
                    recordDate = LocalDate.now().toString(),
                    takenTime = System.currentTimeMillis()
                )
            ).filter { it !is DataResourceResult.Loading }.first()

            _uiState.update { state ->
                if (result is DataResourceResult.Success) {
                    state.copy(
                        medicationList = state.medicationList.map { medication ->
                            if (untakenItems.any {
                                    it.medicationId == medication.medicationId &&
                                        it.scheduleId == medication.scheduleId
                                }
                            ) medication.copy(isTaken = true) else medication
                        }
                    )
                } else {
                    state.copy(userMessage = MedicationUiMessage.IntakeUpdateFailed)
                }
            }
        }
    }

    private fun MedicationScheduleBasis.toUiModel(): MedicationScheduleBasisUiModel = when (this) {
        MedicationScheduleBasis.FIXED_TIME -> MedicationScheduleBasisUiModel.FixedTime
        MedicationScheduleBasis.BEFORE_MEAL -> MedicationScheduleBasisUiModel.BeforeMeal
        MedicationScheduleBasis.AFTER_MEAL -> MedicationScheduleBasisUiModel.AfterMeal
        MedicationScheduleBasis.AS_NEEDED -> MedicationScheduleBasisUiModel.AsNeeded
    }

    private fun MealSlot.toUiModel(): MealSlotUiModel = when (this) {
        MealSlot.BREAKFAST -> MealSlotUiModel.Breakfast
        MealSlot.LUNCH -> MealSlotUiModel.Lunch
        MealSlot.DINNER -> MealSlotUiModel.Dinner
    }

    private fun MealInterval.toUiModel(): MealIntervalUiModel = when (this) {
        MealInterval.IMMEDIATELY -> MealIntervalUiModel.Immediately
        MealInterval.THIRTY_MINUTES -> MealIntervalUiModel.ThirtyMinutes
        MealInterval.ONE_HOUR -> MealIntervalUiModel.OneHour
    }

    private fun RepeatType.toUiModel(): RepeatTypeUiModel = when (this) {
        RepeatType.DAILY -> RepeatTypeUiModel.Daily
        RepeatType.WEEKLY -> RepeatTypeUiModel.Weekly
        RepeatType.PERIOD -> RepeatTypeUiModel.Period
    }

    private fun String.toMinuteOfDay(): Long? {
        val (hour, minute) = split(":").mapNotNull(String::toLongOrNull)
            .takeIf { it.size == 2 }
            ?: return null
        return (hour * 60 + minute).takeIf { it in 0 until 24 * 60 }
    }

    private fun List<TodayMedicationUiModel>.updateMedicationStatus(
        medicationId: String,
        status: MedicationStatusInput
    ) = when (status) {
        MedicationStatusInput.ENDED -> this
        MedicationStatusInput.PAUSED,
        MedicationStatusInput.ACTIVE -> map { item ->
            if (item.medicationId == medicationId) {
                item.copy(isPaused = status == MedicationStatusInput.PAUSED)
            } else {
                item
            }
        }
    }

    private fun MedicationStatus.toInput(): MedicationStatusInput = when (this) {
        MedicationStatus.ACTIVE -> MedicationStatusInput.ACTIVE
        MedicationStatus.PAUSED -> MedicationStatusInput.PAUSED
        MedicationStatus.ENDED -> MedicationStatusInput.ENDED
    }

    private fun MedicationPrimaryTab.includes(type: MedicationTypeUiModel): Boolean = when (this) {
        MedicationPrimaryTab.ALL -> true
        MedicationPrimaryTab.PRESCRIPTION -> type == MedicationTypeUiModel.Prescription
        MedicationPrimaryTab.GENERAL -> type == MedicationTypeUiModel.Otc
        MedicationPrimaryTab.SUPPLEMENTS -> type == MedicationTypeUiModel.Supplement
    }

    // endregion
}
