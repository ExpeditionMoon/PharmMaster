package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.alarm.MedicationAlarm
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.ChangeMedicationStatusUseCase
import com.moon.pharm.domain.usecase.medication.CompleteMedicationGroupUseCase
import com.moon.pharm.domain.usecase.medication.DeleteMedicationUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationsUseCase
import com.moon.pharm.domain.usecase.medication.ObserveTodayMedicationItemsUseCase
import com.moon.pharm.domain.usecase.medication.ObserveWeeklyMedicationAdherenceUseCase
import com.moon.pharm.domain.usecase.medication.SaveMedicationUseCase
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCheckUseCase
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicationViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var medicationRepository: FakeMedicationRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        medicationRepository = FakeMedicationRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `검토 화면의 등록 전에는 복약 데이터를 저장하지 않는다`() = runTest(dispatcher) {
        val viewModel = createViewModel()

        viewModel.onEvent(MedicationUiEvent.UpdateName(name = "타이레놀"))
        advanceUntilIdle()

        assertTrue(medicationRepository.savedMedications.isEmpty())
        assertFalse(viewModel.uiState.value.isMedicationCreated)
    }

    @Test
    fun `검토 화면에서 등록하면 복약 데이터를 저장한다`() = runTest(dispatcher) {
        val viewModel = createViewModel()

        viewModel.onEvent(MedicationUiEvent.UpdateName(name = "타이레놀"))
        viewModel.onEvent(MedicationUiEvent.SaveAllMedications)
        advanceUntilIdle()

        assertEquals(listOf("타이레놀"), medicationRepository.savedMedications.map(Medication::name))
        assertTrue(viewModel.uiState.value.isMedicationCreated)
    }

    @Test
    fun `등록 저장에 실패하면 완료 상태로 전환하지 않고 오류를 표시한다`() = runTest(dispatcher) {
        medicationRepository.saveResult = DataResourceResult.Failure(IllegalStateException())
        val viewModel = createViewModel()

        viewModel.onEvent(MedicationUiEvent.UpdateName(name = "타이레놀"))
        viewModel.onEvent(MedicationUiEvent.SaveAllMedications)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isMedicationCreated)
        assertEquals(MedicationUiMessage.CreateFailed, viewModel.uiState.value.userMessage)
    }

    private fun createViewModel(): MedicationViewModel {
        val alarmScheduler = FakeAlarmScheduler()
        return MedicationViewModel(
            savedStateHandle = SavedStateHandle(),
            observeTodayMedicationItemsUseCase = ObserveTodayMedicationItemsUseCase(medicationRepository),
            observeWeeklyMedicationAdherenceUseCase = ObserveWeeklyMedicationAdherenceUseCase(medicationRepository),
            saveMedicationUseCase = SaveMedicationUseCase(medicationRepository, alarmScheduler),
            changeMedicationStatusUseCase = ChangeMedicationStatusUseCase(medicationRepository, alarmScheduler),
            completeMedicationGroupUseCase = CompleteMedicationGroupUseCase(medicationRepository),
            deleteMedicationUseCase = DeleteMedicationUseCase(medicationRepository, alarmScheduler),
            getMedicationsUseCase = GetMedicationsUseCase(medicationRepository),
            toggleIntakeCheckUseCase = ToggleIntakeCheckUseCase(medicationRepository),
            getCurrentUserIdUseCase = GetCurrentUserIdUseCase(FakeAuthRepository()),
            validateMedicationEntryUseCase = ValidateMedicationEntryUseCase()
        )
    }

    private class FakeMedicationRepository : MedicationRepository {
        var saveResult: DataResourceResult<Unit> = DataResourceResult.Success(Unit)
        val savedMedications = mutableListOf<Medication>()

        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> =
            flowOf(DataResourceResult.Success(emptyList()))

        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> {
            if (saveResult is DataResourceResult.Success) {
                savedMedications += medication
            }
            return flowOf(saveResult)
        }

        override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> =
            flowOf(DataResourceResult.Success(Unit))

        override fun getIntakeRecords(userId: String, date: String): Flow<DataResourceResult<List<IntakeRecord>>> =
            flowOf(DataResourceResult.Success(emptyList()))

        override fun getIntakeRecordsByRange(
            userId: String,
            startDate: String,
            endDate: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = flowOf(DataResourceResult.Success(emptyList()))

        override fun saveIntakeRecord(record: IntakeRecord): Flow<DataResourceResult<Unit>> =
            flowOf(DataResourceResult.Success(Unit))

        override fun deleteIntakeRecord(
            medicationId: String,
            scheduleId: String,
            date: String
        ): Flow<DataResourceResult<Unit>> = flowOf(DataResourceResult.Success(Unit))
    }

    private class FakeAuthRepository : AuthRepository {
        override suspend fun createAccount(email: String, password: String): DataResourceResult<String> =
            DataResourceResult.Success("user-id")

        override suspend fun login(email: String, password: String): DataResourceResult<String> =
            DataResourceResult.Success("user-id")

        override suspend fun logout(): DataResourceResult<Unit> = DataResourceResult.Success(Unit)

        override suspend fun deleteAccount(): DataResourceResult<Unit> = DataResourceResult.Success(Unit)

        override fun getCurrentUserId(): String = "user-id"

        override suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit> =
            DataResourceResult.Success(Unit)
    }

    private class FakeAlarmScheduler : AlarmScheduler {
        override fun schedule(medication: Medication) = Unit

        override fun cancel(medicationId: String) = Unit

        override fun reschedule(alarm: MedicationAlarm) = Unit
    }
}
