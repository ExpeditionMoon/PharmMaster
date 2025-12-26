package com.moon.pharm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moon.pharm.consult.factory.ConsultViewModelFactory
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.ui.screen.EntryPointScreen
import com.moon.pharm.ui.theme.PharmMasterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PharmMasterTheme {
                EntryPointScreen()
            }
        }
    }
}
