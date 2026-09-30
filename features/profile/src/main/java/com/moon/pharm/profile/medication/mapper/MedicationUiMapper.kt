package com.moon.pharm.profile.medication.mapper

import com.moon.pharm.designsystem.util.toScheduleTimeString
import com.moon.pharm.domain.usecase.medication.MealIntervalInput
import com.moon.pharm.domain.usecase.medication.MealSlotInput
import com.moon.pharm.domain.usecase.medication.MealTimingInput
import com.moon.pharm.domain.usecase.medication.MedicationHistoryItem
import com.moon.pharm.domain.usecase.medication.MedicationScheduleBasisInput
import com.moon.pharm.domain.usecase.medication.MedicationScheduleCommandFactory
import com.moon.pharm.domain.usecase.medication.MedicationScheduleItem
import com.moon.pharm.domain.usecase.medication.MedicationTypeInput
import com.moon.pharm.domain.usecase.medication.RepeatTypeInput
import com.moon.pharm.domain.usecase.medication.SaveMedicationCommand
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCommand
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel
import com.moon.pharm.profile.medication.model.MealIntervalUiModel
import com.moon.pharm.profile.medication.model.MealSlotUiModel
import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationScheduleBasisUiModel
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import java.time.LocalDate

object MedicationUiMapper {
    fun toSaveCommand(
        form: MedicationFormState,
        userId: String,
        intakeGroupId: String? = form.intakeGroupId
    ): SaveMedicationCommand {
        return SaveMedicationCommand(
            medicationId = form.medicationId,
            userId = userId,
            name = form.medicationName,
            type = form.selectedType.toInput(),
            startDate = form.startDate ?: System.currentTimeMillis(),
            endDate = if (form.noEndDate) null else form.endDate,
            repeatType = form.selectedRepeatType.toInput(),
            weeklyDays = form.selectedWeeklyDays,
            schedules = MedicationScheduleCommandFactory.create(
                dosage = form.medicationDosage.orEmpty(),
                mealTiming = form.selectedMealTiming.toInput(),
                basis = form.scheduleBasis.toInput(),
                mealSlots = form.selectedMealSlots.map { it.toMealSlotInput() }.toSet(),
                mealInterval = form.mealInterval.toInput(),
                fixedTime = form.selectedTime.toScheduleTimeString()
            ).mapIndexed { index, schedule ->
                schedule.copy(scheduleId = if (index == 0) form.scheduleId else null)
            },
            intakeGroupId = intakeGroupId,
            isGrouped = form.isGrouped,
            isAlarmEnabled = form.isAlarmEnabled,
            status = form.status
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
            intakeGroupId = intakeGroupId,
            scheduleId = scheduleId,
            name = name,
            type = type.toUiModel(),
            repeatType = repeatType.toUiModel(),
            time = time,
            dosage = dosage,
            mealTiming = mealTiming.toUiModel(),
            isPaused = isPaused,
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

    private fun MedicationScheduleBasisUiModel.toInput(): MedicationScheduleBasisInput = when (this) {
        MedicationScheduleBasisUiModel.FixedTime -> MedicationScheduleBasisInput.FIXED_TIME
        MedicationScheduleBasisUiModel.BeforeMeal -> MedicationScheduleBasisInput.BEFORE_MEAL
        MedicationScheduleBasisUiModel.AfterMeal -> MedicationScheduleBasisInput.AFTER_MEAL
        MedicationScheduleBasisUiModel.AsNeeded -> MedicationScheduleBasisInput.AS_NEEDED
    }

    private fun MealSlotUiModel.toMealSlotInput(): MealSlotInput = when (this) {
        MealSlotUiModel.Breakfast -> MealSlotInput.BREAKFAST
        MealSlotUiModel.Lunch -> MealSlotInput.LUNCH
        MealSlotUiModel.Dinner -> MealSlotInput.DINNER
    }

    private fun MealIntervalUiModel.toInput(): MealIntervalInput = when (this) {
        MealIntervalUiModel.Immediately -> MealIntervalInput.IMMEDIATELY
        MealIntervalUiModel.ThirtyMinutes -> MealIntervalInput.THIRTY_MINUTES
        MealIntervalUiModel.OneHour -> MealIntervalInput.ONE_HOUR
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
