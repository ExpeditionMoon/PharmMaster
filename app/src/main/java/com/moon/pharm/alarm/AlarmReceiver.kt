package com.moon.pharm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moon.pharm.domain.alarm.AlarmNotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationService: AlarmNotificationService

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != AlarmConstants.ACTION_MEDICATION_ALARM) return

        val name = intent.getStringExtra(AlarmConstants.EXTRA_MEDICATION_NAME).orEmpty()
        val dosage = intent.getStringExtra(AlarmConstants.EXTRA_DOSAGE).orEmpty()
        val time = intent.getStringExtra(AlarmConstants.EXTRA_ALARM_TIME).orEmpty()
        val isGrouped = intent.getBooleanExtra(AlarmConstants.EXTRA_IS_GROUPED, false)

        notificationService.showMedicationAlarm(name, dosage, time, isGrouped)
    }
}
