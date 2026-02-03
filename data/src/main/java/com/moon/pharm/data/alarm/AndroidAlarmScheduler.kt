package com.moon.pharm.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.moon.pharm.data.common.ALARM_NEXT_DAY_OFFSET
import com.moon.pharm.data.common.KEY_ALARM_TIME
import com.moon.pharm.data.common.KEY_DOSAGE
import com.moon.pharm.data.common.KEY_IS_GROUPED
import com.moon.pharm.data.common.KEY_MEDICATION_NAME
import com.moon.pharm.data.common.TIME_DELIMITER
import com.moon.pharm.data.common.TIME_PARTS_SIZE
import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.model.medication.Medication
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(medication: Medication) {
        medication.schedules.forEach { schedule ->
            val timeParts = schedule.time.split(TIME_DELIMITER)
            if (timeParts.size != TIME_PARTS_SIZE) return@forEach

            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            val localTime = LocalTime.of(hour, minute)
            val alarmId = (medication.id + schedule.id).hashCode()

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(KEY_MEDICATION_NAME, medication.name)
                putExtra(KEY_DOSAGE, schedule.dosage)
                putExtra(KEY_ALARM_TIME, schedule.time)
                putExtra(KEY_IS_GROUPED, medication.isGrouped)
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
                alarmDateTime = alarmDateTime.plusDays(ALARM_NEXT_DAY_OFFSET)
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
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    override fun cancel(medication: Medication) {
        medication.schedules.forEach { schedule ->
            val alarmId = (medication.id + schedule.id).hashCode()

            val intent = Intent(context, AlarmReceiver::class.java)
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