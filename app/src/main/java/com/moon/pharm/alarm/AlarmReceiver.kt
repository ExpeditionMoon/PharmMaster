package com.moon.pharm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moon.pharm.domain.alarm.AlarmNotificationService
import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.alarm.MedicationAlarm
import com.moon.pharm.domain.alarm.MedicationAlarmNextTriggerCalculator
import com.moon.pharm.domain.model.medication.RepeatType
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationService: AlarmNotificationService

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != AlarmConstants.ACTION_MEDICATION_ALARM) return

        val name = intent.getStringExtra(AlarmConstants.EXTRA_MEDICATION_NAME).orEmpty()
        val dosage = intent.getStringExtra(AlarmConstants.EXTRA_DOSAGE).orEmpty()
        val time = intent.getStringExtra(AlarmConstants.EXTRA_ALARM_TIME).orEmpty()
        val isGrouped = intent.getBooleanExtra(AlarmConstants.EXTRA_IS_GROUPED, false)

        val alarm = intent.toMedicationAlarmOrNull()
        if (alarm != null && !MedicationAlarmNextTriggerCalculator.isActiveOn(alarm, LocalDate.now())) return

        alarm?.let(alarmScheduler::reschedule)
        notificationService.showMedicationAlarm(
            name = name,
            dosage = dosage,
            time = time,
            isGrouped = isGrouped,
            groupMedicationNames = alarm?.groupMedicationNames.orEmpty()
                .ifEmpty { listOf(name) }
        )
    }

    private fun Intent.toMedicationAlarmOrNull(): MedicationAlarm? {
        val medicationId = getStringExtra(AlarmConstants.EXTRA_MEDICATION_ID) ?: return null
        val requestCode = getIntExtra(AlarmConstants.EXTRA_REQUEST_CODE, -1)
        if (requestCode < 0) return null

        return MedicationAlarm(
            requestCode = requestCode,
            medicationId = medicationId,
            intakeGroupId = getStringExtra(AlarmConstants.EXTRA_INTAKE_GROUP_ID),
            name = getStringExtra(AlarmConstants.EXTRA_MEDICATION_NAME).orEmpty(),
            groupMedicationNames = getStringArrayListExtra(
                AlarmConstants.EXTRA_GROUP_MEDICATION_NAMES
            ).orEmpty(),
            dosage = getStringExtra(AlarmConstants.EXTRA_DOSAGE).orEmpty(),
            time = getStringExtra(AlarmConstants.EXTRA_ALARM_TIME).orEmpty(),
            isGrouped = getBooleanExtra(AlarmConstants.EXTRA_IS_GROUPED, false),
            repeatType = RepeatType.from(getStringExtra(AlarmConstants.EXTRA_REPEAT_TYPE)),
            weeklyDays = getIntegerArrayListExtra(AlarmConstants.EXTRA_WEEKLY_DAYS).orEmpty().toSet(),
            startDate = getDateExtra(AlarmConstants.EXTRA_START_DATE),
            endDate = getDateExtra(AlarmConstants.EXTRA_END_DATE)
        )
    }

    private fun Intent.getDateExtra(key: String): Long? =
        getLongExtra(key, AlarmConstants.NO_DATE).takeUnless { it == AlarmConstants.NO_DATE }
}
