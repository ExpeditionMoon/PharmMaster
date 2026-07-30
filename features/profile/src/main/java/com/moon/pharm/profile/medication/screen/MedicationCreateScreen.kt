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
import com.moon.pharm.designsystem.component.bar.PharmTopBar
import com.moon.pharm.designsystem.model.TopBarData
import com.moon.pharm.designsystem.model.TopBarNavigationType
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.component.MedicationCreateContent
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel

@Composable
fun MedicationCreateScreen(
    viewModel: MedicationViewModel,
    onNavigateUp: () -> Unit,
    onMedicationCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isMedicationCreated) {
        if (uiState.isMedicationCreated) {
            onMedicationCreated()
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.medication_create_title),
                    navigationType = TopBarNavigationType.Close,
                    onNavigationClick = onNavigateUp
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MedicationCreateContent(
                forms = uiState.medicationForms,
                isLoading = uiState.isLoading,
                onEvent = { intent -> viewModel.onEvent(intent) }
            )
        }
    }
}
