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
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.messaging.FirebaseMessaging
import com.moon.pharm.alarm.AlarmConstants
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.ui.screen.EntryPointScreen
import com.moon.pharm.ui.screen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val isBenchmarkTest = intent.getBooleanExtra("IS_BENCHMARK_TEST", false)
        val targetScreen = intent.getStringExtra("TARGET_SCREEN")

        if (isBenchmarkTest) {
            viewModel.setMockLoginStateForTest(targetScreen)
        } else {
            checkNotificationIntent()

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.refreshFcmToken()
                }
            }
        }

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
                Surface(
                    modifier = Modifier.semantics { testTagsAsResourceId = true }
                ) {
                    val isBenchmarkTest = intent.getBooleanExtra("IS_BENCHMARK_TEST", false)
                    val targetScreen = intent.getStringExtra("TARGET_SCREEN")

                    if (isBenchmarkTest && targetScreen == "MAP") {
                        val testNavController = androidx.navigation.compose.rememberNavController()
                        val testViewModel: com.moon.pharm.consult.viewmodel.ConsultWriteViewModel = androidx.hilt.navigation.compose.hiltViewModel()

                        com.moon.pharm.consult.screen.ConsultPharmacistScreen(
                            navController = testNavController,
                            viewModel = testViewModel,
                            onMapModeChanged = {},
                            startWithMap = true
                        )
                    } else {
                        EntryPointScreen()
                    }
                }
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
}
