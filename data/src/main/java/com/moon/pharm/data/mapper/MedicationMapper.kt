package com.moon.pharm.data.mapper

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.moon.pharm.data.remote.dto.MedicationItemDto
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.domain.model.RepeatType
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate?.toTimestamp(): Timestamp? {
    return this?.let {
        val date = Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
        Timestamp(date)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Timestamp?.toLocalDate(): LocalDate {
    return this?.toDate()?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate() ?: LocalDate.now()
}

@SuppressLint("NewApi")
fun MedicationItem.toFirestoreMedicationDTO(): MedicationItemDto {
    return MedicationItemDto(
        id = this.id,
        name = this.name,
        dosage = this.dosage,
        type = this.type.name,
        startDate = startDate.toTimestamp(),
        endDate = endDate.toTimestamp(),
        noEndDate = this.noEndDate,
        alarmTime = this.alarmTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "",
        mealTiming = this.mealTiming.name,
        repeatType = this.repeatType.name,
        isTaken = this.isTaken
    )
}

@SuppressLint("NewApi")
fun MedicationItemDto.toDomainMedication(): MedicationItem {
    return MedicationItem(
        id = this.id,
        name = this.name,
        dosage = this.dosage,
        type = MedicationType.valueOf(this.type),
        startDate = this.startDate.toLocalDate(),
        endDate = this.endDate?.toLocalDate(),
        noEndDate = this.noEndDate,
        alarmTime = if (this.alarmTime.isNotEmpty()) LocalTime.parse(this.alarmTime) else null,
        mealTiming = MealTiming.valueOf(this.mealTiming),
        repeatType = RepeatType.valueOf(this.repeatType),
        isTaken = this.isTaken
    )
}