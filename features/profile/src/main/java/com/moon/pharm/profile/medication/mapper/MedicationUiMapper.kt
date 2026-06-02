package com.moon.pharm.profile.medication.mapper

import com.moon.pharm.component_ui.util.toScheduleTimeString
import com.moon.pharm.domain.usecase.medication.MealTimingInput
import com.moon.pharm.domain.usecase.medication.MedicationHistoryItem
import com.moon.pharm.domain.usecase.medication.MedicationScheduleCommand
import com.moon.pharm.domain.usecase.medication.MedicationScheduleItem
import com.moon.pharm.domain.usecase.medication.MedicationTypeInput
import com.moon.pharm.domain.usecase.medication.RepeatTypeInput
import com.moon.pharm.domain.usecase.medication.SaveMedicationCommand
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCommand
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel
import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import java.time.LocalDate

object MedicationUiMapper {
    fun toSaveCommand(form: MedicationFormState, userId: String): SaveMedicationCommand {
        return SaveMedicationCommand(
            userId = userId,
            name = form.medicationName,
            type = form.selectedType.toInput(),
            startDate = form.startDate ?: System.currentTimeMillis(),
            endDate = if (form.noEndDate) null else form.endDate,
            repeatType = form.selectedRepeatType.toInput(),
            schedules = listOf(
                MedicationScheduleCommand(
                    time = form.selectedTime.toScheduleTimeString(),
                    dosage = form.medicationDosage.orEmpty(),
                    mealTiming = form.selectedMealTiming.toInput()
                )
            ),
            isGrouped = form.isGrouped
        )
    }

    fun toUiModelList(items: List<MedicationScheduleItem>): List<TodayMedicationUiModel> {
        return items.map { it.toUiModel() }
    }

    fun toHistoryUiModelMap(
        itemsByDate: Map<String, List<MedicationHistoryItem>>
    ): Map<String, List<HistoryRecordUiModel>> {
        return itemsByDate.mapValues { (_, items) ->
            items.map { it.toUiModel() }
        }
    }

    fun toToggleCommand(
        uiModel: TodayMedicationUiModel,
        userId: String,
        isTaken: Boolean,
        date: LocalDate = LocalDate.now()
    ): ToggleIntakeCommand {
        return ToggleIntakeCommand(
            userId = userId,
            medicationId = uiModel.medicationId,
            scheduleId = uiModel.scheduleId,
            recordDate = date.toString(),
            isTaken = isTaken,
            takenTime = if (isTaken) System.currentTimeMillis() else null
        )
    }

    fun toToggleCommand(
        medicationId: String,
        scheduleId: String,
        userId: String,
        isTaken: Boolean,
        date: LocalDate
    ): ToggleIntakeCommand {
        return ToggleIntakeCommand(
            userId = userId,
            medicationId = medicationId,
            scheduleId = scheduleId,
            recordDate = date.toString(),
            isTaken = isTaken,
            takenTime = if (isTaken) System.currentTimeMillis() else null
        )
    }

    private fun MedicationScheduleItem.toUiModel(): TodayMedicationUiModel {
        return TodayMedicationUiModel(
            medicationId = medicationId,
            scheduleId = scheduleId,
            name = name,
            type = type.toUiModel(),
            repeatType = repeatType.toUiModel(),
            time = time,
            dosage = dosage,
            mealTiming = mealTiming.toUiModel(),
            isTaken = isTaken
        )
    }

    private fun MedicationHistoryItem.toUiModel(): HistoryRecordUiModel {
        return HistoryRecordUiModel(
            recordId = recordId,
            medicationId = medicationId,
            scheduleId = scheduleId,
            recordDate = recordDate,
            isTaken = isTaken,
            takenTime = takenTime,
            medicationName = medicationName,
            time = time
        )
    }

    private fun MedicationTypeUiModel.toInput(): MedicationTypeInput {
        return when (this) {
            MedicationTypeUiModel.Prescription -> MedicationTypeInput.PRESCRIPTION
            MedicationTypeUiModel.Otc -> MedicationTypeInput.OTC
            MedicationTypeUiModel.Supplement -> MedicationTypeInput.SUPPLEMENT
        }
    }

    private fun MedicationTypeInput.toUiModel(): MedicationTypeUiModel {
        return when (this) {
            MedicationTypeInput.PRESCRIPTION -> MedicationTypeUiModel.Prescription
            MedicationTypeInput.OTC -> MedicationTypeUiModel.Otc
            MedicationTypeInput.SUPPLEMENT -> MedicationTypeUiModel.Supplement
        }
    }

    private fun MealTimingUiModel.toInput(): MealTimingInput {
        return when (this) {
            MealTimingUiModel.BeforeMeal -> MealTimingInput.BEFORE_MEAL
            MealTimingUiModel.DuringMeal -> MealTimingInput.DURING_MEAL
            MealTimingUiModel.AfterMeal -> MealTimingInput.AFTER_MEAL
            MealTimingUiModel.None -> MealTimingInput.NONE
        }
    }

    private fun MealTimingInput.toUiModel(): MealTimingUiModel {
        return when (this) {
            MealTimingInput.BEFORE_MEAL -> MealTimingUiModel.BeforeMeal
            MealTimingInput.DURING_MEAL -> MealTimingUiModel.DuringMeal
            MealTimingInput.AFTER_MEAL -> MealTimingUiModel.AfterMeal
            MealTimingInput.NONE -> MealTimingUiModel.None
        }
    }

    private fun RepeatTypeUiModel.toInput(): RepeatTypeInput {
        return when (this) {
            RepeatTypeUiModel.Daily -> RepeatTypeInput.DAILY
            RepeatTypeUiModel.Weekly -> RepeatTypeInput.WEEKLY
            RepeatTypeUiModel.Period -> RepeatTypeInput.PERIOD
        }
    }

    private fun RepeatTypeInput.toUiModel(): RepeatTypeUiModel {
        return when (this) {
            RepeatTypeInput.DAILY -> RepeatTypeUiModel.Daily
            RepeatTypeInput.WEEKLY -> RepeatTypeUiModel.Weekly
            RepeatTypeInput.PERIOD -> RepeatTypeUiModel.Period
        }
    }
}
