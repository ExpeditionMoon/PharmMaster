package com.moon.pharm.profile.medication.mapper

import com.moon.pharm.component_ui.util.toDisplayDateString
import com.moon.pharm.component_ui.util.toScheduleTimeString
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel
import com.moon.pharm.profile.medication.screen.MedicationFormState
import java.util.UUID

object MedicationUiMapper {
    fun toDomain(form: MedicationFormState, userId: String): Medication {
        val schedule = MedicationSchedule(
            id = UUID.randomUUID().toString(),
            time = form.selectedTime.toScheduleTimeString(),
            dosage = form.medicationDosage ?: "",
            mealTiming = form.selectedMealTiming
        )

        return Medication(
            id = "",
            userId = userId,
            name = form.medicationName,
            type = form.selectedType,
            startDate = form.startDate ?: System.currentTimeMillis(),
            endDate = if (form.noEndDate) null else form.endDate,
            repeatType = form.selectedRepeatType,
            schedules = listOf(schedule),
            useMealTimeAlarm = form.isMealTimeAlarmEnabled
        )
    }

    fun toUiModelList(medications: List<Medication>): List<TodayMedicationUiModel> {
        return medications.flatMap { medication ->
            medication.schedules.map { schedule ->
                TodayMedicationUiModel(
                    medicationId = medication.id,
                    scheduleId = schedule.id,
                    name = medication.name,
                    type = medication.type,
                    repeatType = medication.repeatType,
                    time = schedule.time,
                    dosage = schedule.dosage,
                    mealTiming = schedule.mealTiming,
                    isTaken = false
                )
            }
        }.sortedBy { it.time }
    }

    fun toIntakeRecord(uiModel: TodayMedicationUiModel, userId: String): IntakeRecord {
        val currentTime = System.currentTimeMillis()
        return IntakeRecord(
            id = "",
            userId = userId,
            medicationId = uiModel.medicationId,
            scheduleId = uiModel.scheduleId,
            recordDate = currentTime.toDisplayDateString(),
            isTaken = uiModel.isTaken,
            takenTime = if (uiModel.isTaken) currentTime else null
        )
    }
}