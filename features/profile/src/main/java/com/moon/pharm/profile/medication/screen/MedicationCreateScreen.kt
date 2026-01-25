package com.moon.pharm.profile.medication.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.component.MedicationCreateContent
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel

@Composable
fun MedicationCreateScreen(
    navController: NavController? = null, viewModel: MedicationViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isMedicationCreated) {
        if (uiState.isMedicationCreated) {
            viewModel.onEvent(MedicationUiEvent.ResetNavigation)
            navController?.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.medication_create_title),
                    navigationType = TopBarNavigationType.Close,
                    onNavigationClick = { navController?.popBackStack() })
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MedicationCreateContent(
                form = uiState.form, onEvent = { intent -> viewModel.onEvent(intent) })
        }
    }
}