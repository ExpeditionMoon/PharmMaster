package com.moon.pharm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.moon.pharm.alarm.AlarmConstants
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.RestoreMedicationAlarmsUseCase
import com.moon.pharm.ui.screen.EntryPointScreen
import com.moon.pharm.ui.screen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var getCurrentUserId: GetCurrentUserIdUseCase

    @Inject
    lateinit var restoreMedicationAlarmsUseCase: RestoreMedicationAlarmsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        restoreMedicationAlarms()
        checkNotificationIntent()

        splashScreen.setKeepOnScreenCondition {
            viewModel.isSplashLoading.value
        }

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
                }.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            PharmMasterTheme {
                EntryPointScreen()
            }
        }
    }

    private fun checkNotificationIntent() {
        val isFromAlarm = intent.getBooleanExtra(AlarmConstants.KEY_IS_FROM_ALARM, false)
        val targetFragment = intent.getStringExtra(AlarmConstants.KEY_TARGET_FRAGMENT)

        if (isFromAlarm && targetFragment == AlarmConstants.FRAGMENT_MEDICATION) {
            viewModel.moveToMedicationTab()
        }
    }

    private fun restoreMedicationAlarms() {
        val userId = getCurrentUserId() ?: return
        lifecycleScope.launch {
            restoreMedicationAlarmsUseCase(userId)
        }
    }
}
