package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.model.medication.MealInterval
import com.moon.pharm.domain.model.medication.MealSlot
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.MedicationScheduleBasis
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.UUID

class SaveMedicationUseCase(
    private val medicationRepository: MedicationRepository,
    private val alarmScheduler: AlarmScheduler
) {
    operator fun invoke(command: SaveMedicationCommand): Flow<DataResourceResult<Unit>> = flow {
        val medications = medicationRepository.getMedications(command.userId)
            .filter { it !is DataResourceResult.Loading }
            .first()
            .let { result -> (result as? DataResourceResult.Success)?.resultData.orEmpty() }
        val medication = command.toMedication()
        val existingMedication = medications.find { it.id == medication.id }
        val grouping = medication.resolveGrouping(medications)

        medicationRepository.saveMedication(grouping.medication).collect { result ->
            if (result is DataResourceResult.Success) {
                grouping.membersToUpdate.forEach { member ->
                    medicationRepository.saveMedication(member).collect { }
                }
                grouping.previousAlarmOwnerIds.forEach(alarmScheduler::cancel)
                existingMedication?.let { previous ->
                    alarmScheduler.cancel(previous.intakeGroupId ?: previous.id)
                }
                alarmScheduler.schedule(
                    medication = grouping.medication,
                    groupMedicationNames = grouping.medicationNames(medications)
                )
            }
            emit(result)
        }
    }

    private fun Medication.resolveGrouping(medications: List<Medication>): GroupingResult {
        if (!isGrouped) return GroupingResult(copy(intakeGroupId = null), emptyList(), emptySet())
        if (intakeGroupId != null) return GroupingResult(this, emptyList(), emptySet())

        val matchingMembers = medications.filter { candidate ->
            candidate.id != id && candidate.hasSameIntakeSchedule(this)
        }
        val groupId = matchingMembers.firstNotNullOfOrNull(Medication::intakeGroupId)
            ?: UUID.randomUUID().toString()

        return GroupingResult(
            medication = copy(intakeGroupId = groupId),
            membersToUpdate = matchingMembers.map { member ->
                member.copy(intakeGroupId = groupId, isGrouped = true)
            },
            previousAlarmOwnerIds = matchingMembers
                .map { member -> member.intakeGroupId ?: member.id }
                .toSet()
        )
    }

    private fun Medication.hasSameIntakeSchedule(other: Medication): Boolean =
        repeatType == other.repeatType &&
            weeklyDays == other.weeklyDays &&
            schedules.map { it.signature() } == other.schedules.map { it.signature() }

    private fun MedicationSchedule.signature(): List<Any?> = listOf(
        time,
        mealTiming,
        basis,
        mealSlot,
        mealInterval
    )

    private data class GroupingResult(
        val medication: Medication,
        val membersToUpdate: List<Medication>,
        val previousAlarmOwnerIds: Set<String>
    ) {
        fun medicationNames(existingMedications: List<Medication>): List<String> {
            val groupId = medication.intakeGroupId ?: return listOf(medication.name)
            return (membersToUpdate + existingMedications)
                .asSequence()
                .filter { member -> member.id != medication.id && member.intakeGroupId == groupId }
                .map(Medication::name)
                .plus(medication.name)
                .distinct()
                .toList()
        }
    }

    private fun SaveMedicationCommand.toMedication(): Medication {
        return Medication(
            id = medicationId ?: UUID.randomUUID().toString(),
            userId = userId,
            name = name,
            type = type.toMedicationType(),
            startDate = startDate,
            endDate = endDate,
            repeatType = repeatType.toRepeatType(),
            weeklyDays = weeklyDays,
            schedules = schedules.map { it.toMedicationSchedule() },
            intakeGroupId = intakeGroupId,
            isGrouped = isGrouped,
            isAlarmEnabled = isAlarmEnabled,
            status = status.toDomain()
        )
    }

    private fun MedicationScheduleCommand.toMedicationSchedule(): MedicationSchedule {
        return MedicationSchedule(
            id = scheduleId ?: UUID.randomUUID().toString(),
            time = time,
            dosage = dosage,
            mealTiming = mealTiming.toMealTiming(),
            basis = basis.toDomain(),
            mealSlot = mealSlot?.toDomain(),
            mealInterval = mealInterval.toDomain()
        )
    }

    private fun MedicationTypeInput.toMedicationType(): MedicationType {
        return when (this) {
            MedicationTypeInput.PRESCRIPTION -> MedicationType.PRESCRIPTION
            MedicationTypeInput.OTC -> MedicationType.OTC
            MedicationTypeInput.SUPPLEMENT -> MedicationType.SUPPLEMENT
        }
    }

    private fun MealTimingInput.toMealTiming(): MealTiming {
        return when (this) {
            MealTimingInput.BEFORE_MEAL -> MealTiming.BEFORE_MEAL
            MealTimingInput.DURING_MEAL -> MealTiming.DURING_MEAL
            MealTimingInput.AFTER_MEAL -> MealTiming.AFTER_MEAL
            MealTimingInput.NONE -> MealTiming.NONE
        }
    }

    private fun MedicationScheduleBasisInput.toDomain(): MedicationScheduleBasis = when (this) {
        MedicationScheduleBasisInput.FIXED_TIME -> MedicationScheduleBasis.FIXED_TIME
        MedicationScheduleBasisInput.BEFORE_MEAL -> MedicationScheduleBasis.BEFORE_MEAL
        MedicationScheduleBasisInput.AFTER_MEAL -> MedicationScheduleBasis.AFTER_MEAL
        MedicationScheduleBasisInput.AS_NEEDED -> MedicationScheduleBasis.AS_NEEDED
    }

    private fun MealSlotInput.toDomain(): MealSlot = when (this) {
        MealSlotInput.BREAKFAST -> MealSlot.BREAKFAST
        MealSlotInput.LUNCH -> MealSlot.LUNCH
        MealSlotInput.DINNER -> MealSlot.DINNER
    }

    private fun MealIntervalInput.toDomain(): MealInterval = when (this) {
        MealIntervalInput.IMMEDIATELY -> MealInterval.IMMEDIATELY
        MealIntervalInput.THIRTY_MINUTES -> MealInterval.THIRTY_MINUTES
        MealIntervalInput.ONE_HOUR -> MealInterval.ONE_HOUR
    }

    private fun RepeatTypeInput.toRepeatType(): RepeatType {
        return when (this) {
            RepeatTypeInput.DAILY -> RepeatType.DAILY
            RepeatTypeInput.WEEKLY -> RepeatType.WEEKLY
            RepeatTypeInput.PERIOD -> RepeatType.PERIOD
        }
    }

    private fun MedicationStatusInput.toDomain(): MedicationStatus = when (this) {
        MedicationStatusInput.ACTIVE -> MedicationStatus.ACTIVE
        MedicationStatusInput.PAUSED -> MedicationStatus.PAUSED
        MedicationStatusInput.ENDED -> MedicationStatus.ENDED
    }
}
