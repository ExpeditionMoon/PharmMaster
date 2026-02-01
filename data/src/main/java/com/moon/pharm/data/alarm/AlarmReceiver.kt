package com.moon.pharm.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moon.pharm.data.common.KEY_ALARM_TIME
import com.moon.pharm.data.common.KEY_DOSAGE
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

        notificationService.showMedicationAlarm(name, dosage, time)
    }
}