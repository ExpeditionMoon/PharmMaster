package com.moon.pharm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.RestoreMedicationAlarmsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmRestoreReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getCurrentUserId: GetCurrentUserIdUseCase

    @Inject
    lateinit var restoreMedicationAlarms: RestoreMedicationAlarmsUseCase

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action !in RESTORE_ACTIONS) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                getCurrentUserId()?.let { userId -> restoreMedicationAlarms(userId) }
            } finally {
                pendingResult.finish()
            }
        }
    }

    private companion object {
        val RESTORE_ACTIONS = setOf(
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED
        )
    }
}
