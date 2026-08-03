package com.moon.pharm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.edit
import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.alarm.MedicationAlarm
import com.moon.pharm.domain.alarm.MedicationAlarmNextTriggerCalculator
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun schedule(medication: Medication) {
        cancel(medication.id)
        if (!medication.isAlarmEnabled || medication.status != MedicationStatus.ACTIVE) return

        medication.schedules.forEach { schedule ->
            val alarm = MedicationAlarm(
                requestCode = nextRequestCode(),
                medicationId = medication.id,
                name = medication.name,
                dosage = schedule.dosage,
                time = schedule.time,
                isGrouped = medication.isGrouped,
                repeatType = medication.repeatType,
                weeklyDays = medication.weeklyDays,
                startDate = medication.startDate,
                endDate = medication.endDate
            )
            if (scheduleNext(alarm)) register(medication.id, alarm.requestCode)
        }
    }

    override fun cancel(medicationId: String) {
        requestCodes(medicationId).forEach { requestCode ->
            alarmManager.cancel(pendingIntent(requestCode))
        }
        preferences.edit { remove(requestCodesKey(medicationId)) }
    }

    override fun reschedule(alarm: MedicationAlarm) {
        if (alarm.requestCode !in requestCodes(alarm.medicationId)) return
        if (!scheduleNext(alarm)) unregister(alarm.medicationId, alarm.requestCode)
    }

    private fun scheduleNext(alarm: MedicationAlarm): Boolean {
        val triggerAt = MedicationAlarmNextTriggerCalculator.nextTriggerAt(alarm, LocalDateTime.now())
            ?.atZone(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
            ?: return false

        val pendingIntent = pendingIntent(alarm)
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
        return true
    }

    private fun pendingIntent(alarm: MedicationAlarm): PendingIntent = PendingIntent.getBroadcast(
        context,
        alarm.requestCode,
        Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmConstants.ACTION_MEDICATION_ALARM
            putExtra(AlarmConstants.EXTRA_REQUEST_CODE, alarm.requestCode)
            putExtra(AlarmConstants.EXTRA_MEDICATION_ID, alarm.medicationId)
            putExtra(AlarmConstants.EXTRA_MEDICATION_NAME, alarm.name)
            putExtra(AlarmConstants.EXTRA_DOSAGE, alarm.dosage)
            putExtra(AlarmConstants.EXTRA_ALARM_TIME, alarm.time)
            putExtra(AlarmConstants.EXTRA_IS_GROUPED, alarm.isGrouped)
            putExtra(AlarmConstants.EXTRA_REPEAT_TYPE, alarm.repeatType.name)
            putIntegerArrayListExtra(AlarmConstants.EXTRA_WEEKLY_DAYS, ArrayList(alarm.weeklyDays))
            putExtra(AlarmConstants.EXTRA_START_DATE, alarm.startDate ?: AlarmConstants.NO_DATE)
            putExtra(AlarmConstants.EXTRA_END_DATE, alarm.endDate ?: AlarmConstants.NO_DATE)
        },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun pendingIntent(requestCode: Int): PendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmConstants.ACTION_MEDICATION_ALARM
        },
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    ) ?: PendingIntent.getBroadcast(
        context,
        requestCode,
        Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmConstants.ACTION_MEDICATION_ALARM
        },
        PendingIntent.FLAG_IMMUTABLE
    )

    private fun nextRequestCode(): Int {
        val next = preferences.getInt(KEY_NEXT_REQUEST_CODE, INITIAL_REQUEST_CODE)
        preferences.edit { putInt(KEY_NEXT_REQUEST_CODE, next + 1) }
        return next
    }

    private fun register(medicationId: String, requestCode: Int) {
        preferences.edit {
            putStringSet(
                requestCodesKey(medicationId),
                requestCodes(medicationId).map(Int::toString).toSet() + requestCode.toString()
            )
        }
    }

    private fun unregister(medicationId: String, requestCode: Int) {
        val remaining = requestCodes(medicationId) - requestCode
        preferences.edit {
            if (remaining.isEmpty()) remove(requestCodesKey(medicationId))
            else putStringSet(requestCodesKey(medicationId), remaining.map(Int::toString).toSet())
        }
    }

    private fun requestCodes(medicationId: String): Set<Int> = preferences
        .getStringSet(requestCodesKey(medicationId), emptySet())
        .orEmpty()
        .mapNotNull(String::toIntOrNull)
        .toSet()

    private fun requestCodesKey(medicationId: String) = "$KEY_REQUEST_CODES_PREFIX$medicationId"

    private companion object {
        const val PREFERENCES_NAME = "medication_alarm_registry"
        const val KEY_NEXT_REQUEST_CODE = "next_request_code"
        const val KEY_REQUEST_CODES_PREFIX = "request_codes_"
        const val INITIAL_REQUEST_CODE = 10_000
    }
}
