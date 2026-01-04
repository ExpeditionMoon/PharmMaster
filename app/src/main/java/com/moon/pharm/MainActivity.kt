package com.moon.pharm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.moon.pharm.ui.screen.EntryPointScreen
import com.moon.pharm.ui.screen.MainViewModel
import com.moon.pharm.ui.theme.PharmMasterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val  viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

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
}
