package com.moon.pharm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.moon.pharm.alarm.AlarmConstants
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.ui.screen.EntryPointScreen
import com.moon.pharm.ui.screen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        checkNotificationIntent()

        splashScreen.setKeepOnScreenCondition {
            viewModel.isSplashLoading.value
        }

        enableEdgeToEdge()

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

    override fun onResume() {
        super.onResume()
        viewModel.restoreMedicationAlarms()
    }
}
