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

class ObserveTodayMedicationItemsUseCaseTest {

    @Test
    fun `keeps an ended medication visible on its end date`() = runBlocking {
        val endDate = LocalDate.of(2026, 8, 3)
        val useCase = ObserveTodayMedicationItemsUseCase(
            repository = FakeMedicationRepository(listOf(endedMedication(endDate)))
        )

        val result = useCase("user-id", endDate)
            .filterIsInstance<DataResourceResult.Success<List<MedicationScheduleItem>>>()
            .first()
            .resultData

        assertEquals(1, result.size)
    }

    @Test
    fun `hides an ended medication after its end date`() = runBlocking {
        val endDate = LocalDate.of(2026, 8, 3)
        val useCase = ObserveTodayMedicationItemsUseCase(
            repository = FakeMedicationRepository(listOf(endedMedication(endDate)))
        )

        val result = useCase("user-id", endDate.plusDays(1))
            .filterIsInstance<DataResourceResult.Success<List<MedicationScheduleItem>>>()
            .first()
            .resultData

        assertEquals(emptyList<MedicationScheduleItem>(), result)
    }

    private fun endedMedication(endDate: LocalDate) = Medication(
        id = "medication-id",
        userId = "user-id",
        name = "약 이름",
        type = MedicationType.PRESCRIPTION,
        startDate = LocalDate.of(2026, 8, 1).toEpochMillis(),
        endDate = endDate.toEpochMillis(),
        repeatType = RepeatType.DAILY,
        weeklyDays = emptySet(),
        status = MedicationStatus.ENDED,
        schedules = listOf(
            MedicationSchedule(
                id = "schedule-id",
                time = "09:00",
                dosage = "1정",
                mealTiming = MealTiming.AFTER_MEAL
            )
        )
    )

    private fun LocalDate.toEpochMillis(): Long =
        atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    private class FakeMedicationRepository(
        private val medications: List<Medication>
    ) : MedicationRepository {
        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> =
            flowOf(DataResourceResult.Success(medications))

        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> = unexpectedCall()

        override fun getIntakeRecords(
            userId: String,
            date: String
        ): Flow<DataResourceResult<List<IntakeRecord>>> = flowOf(DataResourceResult.Success(emptyList()))

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

        private fun <T> unexpectedCall(): Flow<DataResourceResult<T>> {
            throw AssertionError("Unexpected repository call")
        }
    }
}
