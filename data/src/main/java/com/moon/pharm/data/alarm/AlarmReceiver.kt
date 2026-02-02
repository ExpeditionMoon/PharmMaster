package com.moon.pharm.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moon.pharm.data.common.KEY_ALARM_TIME
import com.moon.pharm.data.common.KEY_DOSAGE
import com.moon.pharm.data.common.KEY_IS_GROUPED
import com.moon.pharm.data.common.KEY_MEDICATION_NAME
import com.moon.pharm.domain.alarm.AlarmNotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationService: AlarmNotificationService

    override fun onReceive(context: Context, intent: Intent?) {
        val name = intent?.getStringExtra(KEY_MEDICATION_NAME) ?: ""
        val dosage = intent?.getStringExtra(KEY_DOSAGE) ?: ""
        val time = intent?.getStringExtra(KEY_ALARM_TIME) ?: ""
        val isGrouped = intent?.getBooleanExtra(KEY_IS_GROUPED, false) ?: false

        notificationService.showMedicationAlarm(name, dosage, time, isGrouped)
    }
}