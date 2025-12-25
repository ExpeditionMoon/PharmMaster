package com.moon.pharm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moon.pharm.consult.factory.ConsultViewModelFactory
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.di.AppContainerProvider
import com.moon.pharm.ui.screen.EntryPointScreen
import com.moon.pharm.ui.theme.PharmMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PharmMasterTheme {
                val applicationContext = LocalContext.current.applicationContext
                val consultUseCases = (applicationContext as AppContainerProvider)
                    .provideAppContainer()
                    .consultUseCases
                val factory = ConsultViewModelFactory(consultUseCases)

                val viewModel: ConsultViewModel = viewModel(factory = factory)

                EntryPointScreen(viewModel)
            }
        }
    }
}
