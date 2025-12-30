package com.moon.pharm.profile.screen.medication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.view.SelectButton
import com.moon.pharm.profile.viewmodel.MedicationIntent
import com.moon.pharm.profile.viewmodel.MedicationViewModel

@Composable
fun MedicationCreateScreen(
    navController: NavController? = null, viewModel: MedicationViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MedicationCreateContent(
        form = uiState.form,
        onIntent = { intent -> viewModel.onIntent(intent) }
    )
}
@Composable
fun MedicationCreateContent(
    form: MedicationForm,
    onIntent: (MedicationIntent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        MedicationInfoSection(
            form = form,
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.height(10.dp))

        MedicationAlarmSection(
            form = form,
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.height(20.dp))

        SelectButton(
            text = "등록하기",
            isSelected = form.medicationName.isNotEmpty(),
            onClick = { onIntent(MedicationIntent.SaveMedication) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}