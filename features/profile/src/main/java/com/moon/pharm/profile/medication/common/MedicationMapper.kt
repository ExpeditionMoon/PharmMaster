package com.moon.pharm.profile.medication.common

import com.moon.pharm.component_ui.util.toDisplayTimeString
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.profile.medication.screen.MedicationFormState
import java.util.UUID

fun MedicationFormState.toMedication(userId: String): Medication {
    // ID는 신규 생성 시 비워두거나 UUID 생성 (서버/DB 로직에 따라 다름, 여기선 빈 문자열 가정)
    // Schedule 객체 생성
    val schedule = MedicationSchedule(
        id = UUID.randomUUID().toString(), // 스케줄 ID 생성
        time = selectedTime.toDisplayTimeString(), // Long -> "HH:mm" 변환 필요
        dosage = medicationDosage ?: "",
        mealTiming = selectedMealTiming
    )

    return Medication(
        id = "", // Firestore가 ID 자동 생성시 빈값, 아니면 UUID
        userId = userId, // UseCase 호출 직전에 채워넣음
        name = medicationName,
        type = selectedType,
        startDate = startDate,
        endDate = if (noEndDate) null else endDate,
        repeatType = selectedRepeatType,
        schedules = listOf(schedule), // 리스트로 감싸기
        useMealTimeAlarm = isMealTimeAlarmEnabled
    )
}