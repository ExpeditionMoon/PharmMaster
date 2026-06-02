package com.moon.pharm.profile.medication.mapper

import com.moon.pharm.component_ui.util.toScheduleTimeString
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel
import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import java.time.LocalDate
import java.util.UUID

object MedicationUiMapper {
    fun toDomain(form: MedicationFormState, userId: String): Medication {
        val schedule = MedicationSchedule(
            id = UUID.randomUUID().toString(),
            time = form.selectedTime.toScheduleTimeString(),
            dosage = form.medicationDosage ?: "",
            mealTiming = form.selectedMealTiming.toDomainModel()
        )

        return Medication(
            id = "",
            userId = userId,
            name = form.medicationName,
            type = form.selectedType.toDomainModel(),
            startDate = form.startDate ?: System.currentTimeMillis(),
            endDate = if (form.noEndDate) null else form.endDate,
            repeatType = form.selectedRepeatType.toDomainModel(),
            schedules = listOf(schedule),
            isGrouped = form.isGrouped
        )
    }

    fun toUiModelList(medications: List<Medication>): List<TodayMedicationUiModel> {
        return medications.flatMap { medication ->
            medication.schedules.map { schedule ->
                TodayMedicationUiModel(
                    medicationId = medication.id,
                    scheduleId = schedule.id,
                    name = medication.name,
                    type = medication.type.toUiModel(),
                    repeatType = medication.repeatType.toUiModel(),
                    time = schedule.time,
                    dosage = schedule.dosage,
                    mealTiming = schedule.mealTiming.toUiModel(),
                    isTaken = false
                )
            }
        }.sortedBy { it.time }
    }

    fun toIntakeRecord(uiModel: TodayMedicationUiModel, userId: String): IntakeRecord {
        val currentTime = System.currentTimeMillis()
        val standardDate = LocalDate.now().toString()
        return IntakeRecord(
            id = "",
            userId = userId,
            medicationId = uiModel.medicationId,
            scheduleId = uiModel.scheduleId,
            recordDate = standardDate,
            isTaken = uiModel.isTaken,
            takenTime = if (uiModel.isTaken) currentTime else null
        )
    }

    fun toHistoryUiModel(
        record: IntakeRecord,
        medicationName: String,
        time: String
    ): HistoryRecordUiModel {
        return HistoryRecordUiModel(
            recordId = record.id,
            medicationId = record.medicationId,
            scheduleId = record.scheduleId,
            recordDate = record.recordDate,
            isTaken = record.isTaken,
            takenTime = record.takenTime,
            medicationName = medicationName,
            time = time
        )
    }

    private fun MedicationType.toUiModel(): MedicationTypeUiModel {
        return when (this) {
            MedicationType.PRESCRIPTION -> MedicationTypeUiModel.Prescription
            MedicationType.OTC -> MedicationTypeUiModel.Otc
            MedicationType.SUPPLEMENT -> MedicationTypeUiModel.Supplement
        }
    }

    private fun MedicationTypeUiModel.toDomainModel(): MedicationType {
        return when (this) {
            MedicationTypeUiModel.Prescription -> MedicationType.PRESCRIPTION
            MedicationTypeUiModel.Otc -> MedicationType.OTC
            MedicationTypeUiModel.Supplement -> MedicationType.SUPPLEMENT
        }
    }

    private fun MealTiming.toUiModel(): MealTimingUiModel {
        return when (this) {
            MealTiming.BEFORE_MEAL -> MealTimingUiModel.BeforeMeal
            MealTiming.DURING_MEAL -> MealTimingUiModel.DuringMeal
            MealTiming.AFTER_MEAL -> MealTimingUiModel.AfterMeal
            MealTiming.NONE -> MealTimingUiModel.None
        }
    }

    private fun MealTimingUiModel.toDomainModel(): MealTiming {
        return when (this) {
            MealTimingUiModel.BeforeMeal -> MealTiming.BEFORE_MEAL
            MealTimingUiModel.DuringMeal -> MealTiming.DURING_MEAL
            MealTimingUiModel.AfterMeal -> MealTiming.AFTER_MEAL
            MealTimingUiModel.None -> MealTiming.NONE
        }
    }

    private fun RepeatType.toUiModel(): RepeatTypeUiModel {
        return when (this) {
            RepeatType.DAILY -> RepeatTypeUiModel.Daily
            RepeatType.WEEKLY -> RepeatTypeUiModel.Weekly
            RepeatType.PERIOD -> RepeatTypeUiModel.Period
        }
    }

    private fun RepeatTypeUiModel.toDomainModel(): RepeatType {
        return when (this) {
            RepeatTypeUiModel.Daily -> RepeatType.DAILY
            RepeatTypeUiModel.Weekly -> RepeatType.WEEKLY
            RepeatTypeUiModel.Period -> RepeatType.PERIOD
        }
    }
}
