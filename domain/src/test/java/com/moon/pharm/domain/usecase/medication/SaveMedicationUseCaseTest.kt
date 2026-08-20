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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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

    @Test
    fun `schedules an alarm before a success result is consumed`() = runBlocking {
        val repository = RecordingMedicationRepository()
        val alarmScheduler = RecordingAlarmScheduler()

        SaveMedicationUseCase(repository, alarmScheduler)(saveMedicationCommand())
            .filter { it is DataResourceResult.Success }
            .first()

        assertNotNull(alarmScheduler.scheduledMedication)
    }

    @Test
    fun `keeps medication and schedule identifiers when medication is edited`() = runBlocking {
        val repository = RecordingMedicationRepository()
        val alarmScheduler = RecordingAlarmScheduler()
        val useCase = SaveMedicationUseCase(repository, alarmScheduler)
        val command = saveMedicationCommand().copy(
            medicationId = "medication-id",
            schedules = listOf(
                MedicationScheduleCommand(
                    scheduleId = "schedule-id",
                    time = "10:00",
                    dosage = "2정",
                    mealTiming = MealTimingInput.AFTER_MEAL
                )
            )
        )

        useCase(command).toList()

        assertEquals("medication-id", repository.savedMedication?.id)
        assertEquals("schedule-id", repository.savedMedication?.schedules?.single()?.id)
    }

    @Test
    fun `cancels the previous alarm before scheduling an edited medication`() = runBlocking {
        val existingMedication = Medication(
            id = "medication-id",
            userId = "user-id",
            name = "기존 약",
            type = MedicationType.PRESCRIPTION,
            startDate = 1_000L,
            endDate = null,
            repeatType = RepeatType.DAILY,
            schedules = emptyList(),
            intakeGroupId = "group-id"
        )
        val repository = RecordingMedicationRepository(existingMedication)
        val alarmScheduler = RecordingAlarmScheduler()

        SaveMedicationUseCase(repository, alarmScheduler)(
            saveMedicationCommand().copy(medicationId = existingMedication.id)
        ).toList()

        assertEquals("group-id", alarmScheduler.cancelledMedicationId)
    }

    @Test
    fun `keeps paused status when medication is edited`() = runBlocking {
        val repository = RecordingMedicationRepository()
        val alarmScheduler = RecordingAlarmScheduler()
        val useCase = SaveMedicationUseCase(repository, alarmScheduler)

        useCase(saveMedicationCommand().copy(status = MedicationStatusInput.PAUSED)).toList()

        assertEquals(MedicationStatus.PAUSED, repository.savedMedication?.status)
    }

    @Test
    fun `groups an existing medication with the same intake schedule`() = runBlocking {
        val existingMedication = Medication(
            id = "existing-medication-id",
            userId = "user-id",
            name = "기존 약",
            type = MedicationType.PRESCRIPTION,
            startDate = 1_000L,
            endDate = null,
            repeatType = RepeatType.DAILY,
            schedules = listOf(
                MedicationSchedule(
                    id = "existing-schedule-id",
                    time = "09:00",
                    dosage = "1정",
                    mealTiming = MealTiming.AFTER_MEAL
                )
            )
        )
        val repository = RecordingMedicationRepository(existingMedication)

        val alarmScheduler = RecordingAlarmScheduler()

        SaveMedicationUseCase(repository, alarmScheduler)(saveMedicationCommand())
            .toList()

        val groupedMedications = repository.savedMedications

        assertEquals(2, groupedMedications.size)
        assertTrue(groupedMedications.all { it.isGrouped })
        assertEquals(1, groupedMedications.map(Medication::intakeGroupId).distinct().size)
        assertEquals(setOf("약 이름", "기존 약"), alarmScheduler.groupMedicationNames.toSet())
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
        var cancelledMedicationId: String? = null
        var groupMedicationNames: List<String> = emptyList()

        override fun schedule(medication: Medication) {
            scheduledMedication = medication
        }

        override fun schedule(medication: Medication, groupMedicationNames: List<String>) {
            scheduledMedication = medication
            this.groupMedicationNames = groupMedicationNames
        }

        override fun cancel(medicationId: String) {
            cancelledMedicationId = medicationId
        }

        override fun reschedule(alarm: MedicationAlarm) = Unit
    }

    private class RecordingMedicationRepository : MedicationRepository {
        constructor(existingMedication: Medication? = null) {
            medications = existingMedication?.let(::listOf).orEmpty()
        }

        private val medications: List<Medication>
        var savedMedication: Medication? = null
        val savedMedications = mutableListOf<Medication>()

        override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> =
            flowOf(DataResourceResult.Success(medications))

        override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> {
            savedMedication = medication
            savedMedications += medication
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
