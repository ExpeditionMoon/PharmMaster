package com.moon.pharm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.model.medication.Medication
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(medication: Medication) {
        medication.schedules.forEach { schedule ->
            val timeParts = schedule.time.split(AlarmConstants.TIME_DELIMITER)
            if (timeParts.size != AlarmConstants.TIME_PARTS_SIZE) return@forEach

            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            val localTime = LocalTime.of(hour, minute)
            val alarmId = (medication.id + schedule.id).hashCode()

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmConstants.ACTION_MEDICATION_ALARM
                putExtra(AlarmConstants.EXTRA_MEDICATION_NAME, medication.name)
                putExtra(AlarmConstants.EXTRA_DOSAGE, schedule.dosage)
                putExtra(AlarmConstants.EXTRA_ALARM_TIME, schedule.time)
                putExtra(AlarmConstants.EXTRA_IS_GROUPED, medication.isGrouped)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val now = LocalDateTime.now()
            var alarmDateTime = LocalDateTime.of(now.toLocalDate(), localTime)

            if (alarmDateTime.isBefore(now)) {
                alarmDateTime = alarmDateTime.plusDays(AlarmConstants.NEXT_DAY_OFFSET)
            }

            val triggerAtMillis = alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            triggerAtMillis,
                            pendingIntent
                        )
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } catch (_: SecurityException) {
                return@forEach
            }
        }
    }

    override fun cancel(medication: Medication) {
        medication.schedules.forEach { schedule ->
            val alarmId = (medication.id + schedule.id).hashCode()

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmConstants.ACTION_MEDICATION_ALARM
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)
        }
    }
}
