package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.alarm.MedicationAlarm
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChangeMedicationStatusUseCaseTest {

    @Test
    fun `pausing medication saves paused status and cancels its alarm`() = runBlocking {
        val repository = RecordingMedicationRepository()
        val scheduler = RecordingAlarmScheduler()
        val useCase = ChangeMedicationStatusUseCase(repository, scheduler)

        useCase(changeStatusCommand(MedicationStatusInput.PAUSED)).toList()

        assertEquals(MedicationStatus.PAUSED, repository.savedMedication?.status)
        assertEquals(MEDICATION_ID, scheduler.cancelledMedicationId)
        assertNull(scheduler.scheduledMedication)
    }

    @Test
    fun `ending medication keeps history period and cancels its alarm`() = runBlocking {
        val repository = RecordingMedicationRepository()
        val scheduler = RecordingAlarmScheduler()
        val useCase = ChangeMedicationStatusUseCase(repository, scheduler)

        useCase(changeStatusCommand(MedicationStatusInput.ENDED)).toList()

        assertEquals(MedicationStatus.ENDED, repository.savedMedication?.status)
        assertEquals(CHANGED_AT, repository.savedMedication?.endDate)
        assertEquals(MEDICATION_ID, scheduler.cancelledMedicationId)
    }

    @Test
    fun `resuming medication schedules alarm after status is saved`() = runBlocking {
        val repository = RecordingMedicationRepository(status = MedicationStatus.PAUSED)
        val scheduler = RecordingAlarmScheduler()
        val useCase = ChangeMedicationStatusUseCase(repository, scheduler)

        useCase(changeStatusCommand(MedicationStatusInput.ACTIVE)).toList()

        assertEquals(MedicationStatus.ACTIVE, repository.savedMedication?.status)
        assertEquals(MEDICATION_ID, scheduler.scheduledMedication?.id)
        assertNull(scheduler.cancelledMedicationId)
    }

    private fun changeStatusCommand(status: MedicationStatusInput) = ChangeMedicationStatusCommand(
        userId = USER_ID,
        medicationId = MEDICATION_ID,
        status = status,
        changedAt = CHANGED_AT
    )

    private class RecordingAlarmScheduler : AlarmScheduler {
        var scheduledMedication: Medication? = null
        var cancelledMedicationId: String? = null

        override fun schedule(medication: Medication) {
            scheduledMedication = medication
        }

        override fun cancel(medicationId: String) {
            cancelledMedicationId = medicationId
        }

        override fun reschedule(alarm: MedicationAlarm) = Unit
    }

    private class RecordingMedicationRepository(
        status: MedicationStatus = MedicationStatus.ACTIVE
    ) : MedicationRepository {
        private val medication = Medication(
            id = MEDICATION_ID,
            userId = USER_ID,
            name = "테스트 약",
            type = MedicationType.PRESCRIPTION,
            startDate = 1_000L,
            endDate = null,
            repeatType = RepeatType.DAILY,
            schedules = listOf(
                MedicationSchedule(
                    id = "schedule-id",
                    time = "09:00",
                    dosage = "1정",
                    mealTiming = MealTiming.AFTER_MEAL
                )
            ),
            status = status
        )

        var savedMedication: Medication? = null

        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> =
            flowOf(DataResourceResult.Success(listOf(medication)))

        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> {
            savedMedication = medication
            return flowOf(DataResourceResult.Success(Unit))
        }

        override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun getIntakeRecords(
            userId: String,
            date: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = unexpectedCall()

        override fun getIntakeRecordsByRange(
            userId: String,
            startDate: String,
            endDate: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = unexpectedCall()

        override fun saveIntakeRecord(record: IntakeRecord): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun deleteIntakeRecord(
            medicationId: String,
            scheduleId: String,
            date: String
        ): Flow<DataResourceResult<Unit>> = unexpectedCall()

        private fun <T> unexpectedCall(): Flow<DataResourceResult<T>> =
            throw AssertionError("Unexpected repository call")
    }

    private companion object {
        const val USER_ID = "user-id"
        const val MEDICATION_ID = "medication-id"
        const val CHANGED_AT = 2_000L
    }
}
