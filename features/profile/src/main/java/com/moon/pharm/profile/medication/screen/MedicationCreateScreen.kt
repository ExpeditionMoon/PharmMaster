package com.moon.pharm.profile.medication.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.mapper.asMedicationString
import com.moon.pharm.profile.medication.screen.component.MedicationCreateContent
import com.moon.pharm.profile.medication.viewmodel.MedicationEffect
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel

@Composable
fun MedicationCreateScreen(
    viewModel: MedicationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMedicationHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MedicationEffect.ShowMessage -> snackbarHostState.showSnackbar(
                    effect.message.asMedicationString(context)
                )
                MedicationEffect.NavigateMedicationHome -> {
                    onNavigateToMedicationHome()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.medication_create_title),
                    navigationType = TopBarNavigationType.Close,
                    onNavigationClick = onNavigateBack
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
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
