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
import org.junit.Test

class DeleteMedicationUseCaseTest {

    @Test
    fun `cancels medication alarms after medication is deleted`() = runBlocking {
        val scheduler = RecordingAlarmScheduler()
        val useCase = DeleteMedicationUseCase(DeletingMedicationRepository(), scheduler)

        useCase("medication-id").toList()

        assertEquals("medication-id", scheduler.cancelledMedicationId)
    }

    private class RecordingAlarmScheduler : AlarmScheduler {
        var cancelledMedicationId: String? = null

        override fun schedule(medication: Medication) = Unit

        override fun cancel(medicationId: String) {
            cancelledMedicationId = medicationId
        }

        override fun reschedule(alarm: MedicationAlarm) = Unit
    }

    private class DeletingMedicationRepository : MedicationRepository {
        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> = unexpectedCall()

        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> =
            flowOf(DataResourceResult.Success(Unit))

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
}
