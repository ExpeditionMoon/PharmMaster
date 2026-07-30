package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.alarm.MedicationAlarm
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SaveMedicationUseCaseTest {

    @Test
    fun `schedules an alarm after medication is saved`() = runBlocking {
        val repository = RecordingMedicationRepository()
        val alarmScheduler = RecordingAlarmScheduler()
        val useCase = SaveMedicationUseCase(repository, alarmScheduler)

        useCase(saveMedicationCommand()).toList()

        val scheduledMedication = alarmScheduler.scheduledMedication

        assertNotNull(scheduledMedication)
        assertEquals(repository.savedMedication, scheduledMedication)
    }

    private fun saveMedicationCommand() = SaveMedicationCommand(
        userId = "user-id",
        name = "약 이름",
        type = MedicationTypeInput.PRESCRIPTION,
        startDate = 1_000L,
        endDate = null,
        repeatType = RepeatTypeInput.DAILY,
        schedules = listOf(
            MedicationScheduleCommand(
                time = "09:00",
                dosage = "1정",
                mealTiming = MealTimingInput.AFTER_MEAL
            )
        ),
        isGrouped = true
    )

    private class RecordingAlarmScheduler : AlarmScheduler {
        var scheduledMedication: Medication? = null

        override fun schedule(medication: Medication) {
            scheduledMedication = medication
        }

        override fun cancel(medicationId: String) = Unit

        override fun reschedule(alarm: MedicationAlarm) = Unit
    }

    private class RecordingMedicationRepository : MedicationRepository {
        var savedMedication: Medication? = null

        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> =
            unexpectedCall(userId)

        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> {
            savedMedication = medication
            return flowOf(DataResourceResult.Success(Unit))
        }

        override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> =
            unexpectedCall(medicationId)

        override fun getIntakeRecords(
            userId: String,
            date: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = unexpectedCall(userId, date)

        override fun getIntakeRecordsByRange(
            userId: String,
            startDate: String,
            endDate: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = unexpectedCall(userId, startDate, endDate)

        override fun saveIntakeRecord(record: IntakeRecord): Flow<DataResourceResult<Unit>> =
            unexpectedCall(record)

        override fun deleteIntakeRecord(
            medicationId: String,
            scheduleId: String,
            date: String
        ): Flow<DataResourceResult<Unit>> = unexpectedCall(medicationId, scheduleId, date)

        private fun <T> unexpectedCall(vararg arguments: Any?): Flow<DataResourceResult<T>> {
            throw AssertionError("예상하지 못한 repository 호출: ${arguments.contentToString()}")
        }
    }
}
