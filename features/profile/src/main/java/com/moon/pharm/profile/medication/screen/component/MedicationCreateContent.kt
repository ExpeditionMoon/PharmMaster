package com.moon.pharm.profile.medication.screen.component

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import com.moon.pharm.profile.medication.screen.section.MedicationAlarmSection
import com.moon.pharm.profile.medication.screen.section.MedicationInfoSection
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

@Composable
fun MedicationCreateContent(
    form: MedicationFormState, onEvent: (MedicationUiEvent) -> Unit
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
            form = form, onEvent = onEvent
        )

        Spacer(modifier = Modifier.height(10.dp))

        MedicationAlarmSection(
            form = form, onEvent = onEvent
        )

        Spacer(modifier = Modifier.height(20.dp))

        PharmPrimaryButton(
            text = stringResource(R.string.medication_add),
            onClick = { onEvent(MedicationUiEvent.SaveMedication) },
            enabled = form.medicationName.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}