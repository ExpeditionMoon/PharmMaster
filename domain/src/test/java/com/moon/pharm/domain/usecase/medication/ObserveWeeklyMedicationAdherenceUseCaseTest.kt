package com.moon.pharm.domain.usecase.medication

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
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class ObserveWeeklyMedicationAdherenceUseCaseTest {

    @Test
    fun `calculates the adherence from this weeks scheduled and completed doses`() = runBlocking {
        val today = LocalDate.of(2026, 8, 5)
        val useCase = ObserveWeeklyMedicationAdherenceUseCase(
            repository = FakeMedicationRepository(
                medications = listOf(
                    medication(
                        id = "daily",
                        repeatType = RepeatType.DAILY
                    ),
                    medication(
                        id = "weekly",
                        repeatType = RepeatType.WEEKLY,
                        weeklyDays = setOf(3)
                    )
                ),
                records = listOf(
                    intakeRecord("daily", "2026-08-03"),
                    intakeRecord("daily", "2026-08-04")
                )
            )
        )

        val result = useCase("user-id", today)
            .filterIsInstance<DataResourceResult.Success<WeeklyMedicationAdherence>>()
            .first()
            .resultData

        assertEquals(4, result.totalCount)
        assertEquals(2, result.completedCount)
    }

    private fun medication(
        id: String,
        repeatType: RepeatType,
        weeklyDays: Set<Int> = emptySet()
    ) = Medication(
        id = id,
        userId = "user-id",
        name = id,
        type = MedicationType.PRESCRIPTION,
        startDate = LocalDate.of(2026, 8, 3).toEpochMillis(),
        endDate = null,
        repeatType = repeatType,
        weeklyDays = weeklyDays,
        status = MedicationStatus.ACTIVE,
        schedules = listOf(
            MedicationSchedule(
                id = "schedule-id",
                time = "09:00",
                dosage = "1정",
                mealTiming = MealTiming.AFTER_MEAL
            )
        )
    )

    private fun intakeRecord(medicationId: String, recordDate: String) = IntakeRecord(
        id = "$medicationId-$recordDate",
        userId = "user-id",
        medicationId = medicationId,
        scheduleId = "schedule-id",
        recordDate = recordDate,
        isTaken = true
    )

    private fun LocalDate.toEpochMillis(): Long =
        atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    private class FakeMedicationRepository(
        private val medications: List<Medication>,
        private val records: List<IntakeRecord>
    ) : MedicationRepository {
        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> =
            flowOf(DataResourceResult.Success(medications))

        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun getIntakeRecords(
            userId: String,
            date: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = unexpectedCall()

        override fun getIntakeRecordsByRange(
            userId: String,
            startDate: String,
            endDate: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = flowOf(DataResourceResult.Success(records))

        override fun saveIntakeRecord(record: IntakeRecord): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun deleteIntakeRecord(
            medicationId: String,
            scheduleId: String,
            date: String
        ): Flow<DataResourceResult<Unit>> = unexpectedCall()

        private fun <T> unexpectedCall(): Flow<DataResourceResult<T>> {
            throw AssertionError("Unexpected repository call")
        }
    }
}
