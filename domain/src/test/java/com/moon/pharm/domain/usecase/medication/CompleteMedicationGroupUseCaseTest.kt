package com.moon.pharm.domain.usecase.medication

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

class CompleteMedicationGroupUseCaseTest {

    @Test
    fun `saves a separate intake record for every group member`() = runBlocking {
        val repository = RecordingMedicationRepository()
        val result = CompleteMedicationGroupUseCase(repository)(
            CompleteMedicationGroupCommand(
                userId = "user-id",
                items = listOf(
                    MedicationGroupIntakeItem("medication-a", "schedule-a"),
                    MedicationGroupIntakeItem("medication-b", "schedule-b")
                ),
                recordDate = "2026-08-20",
                takenTime = 100L
            )
        ).toList()

        assertEquals(2, repository.savedRecords.size)
        assertEquals(
            setOf("medication-a" to "schedule-a", "medication-b" to "schedule-b"),
            repository.savedRecords.map { it.medicationId to it.scheduleId }.toSet()
        )
        assertEquals(DataResourceResult.Success(Unit), result.last())
    }

    private class RecordingMedicationRepository : MedicationRepository {
        val savedRecords = mutableListOf<IntakeRecord>()

        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> = unexpectedCall()
        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> = unexpectedCall()
        override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> = unexpectedCall()
        override fun getIntakeRecords(userId: String, date: String): Flow<DataResourceResult<List<IntakeRecord>>> = unexpectedCall()
        override fun getIntakeRecordsByRange(userId: String, startDate: String, endDate: String): Flow<DataResourceResult<List<IntakeRecord>>> = unexpectedCall()
        override fun saveIntakeRecord(record: IntakeRecord): Flow<DataResourceResult<Unit>> {
            savedRecords += record
            return flowOf(DataResourceResult.Success(Unit))
        }
        override fun deleteIntakeRecord(medicationId: String, scheduleId: String, date: String): Flow<DataResourceResult<Unit>> = unexpectedCall()

        private fun <T> unexpectedCall(): Flow<DataResourceResult<T>> =
            throw AssertionError("Unexpected repository call")
    }
}
