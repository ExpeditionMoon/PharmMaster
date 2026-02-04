package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.section.MedicationAlarmSection
import com.moon.pharm.profile.medication.screen.section.MedicationInfoSection
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

@Composable
fun MedicationCreateContent(
    forms: List<MedicationFormState>,
    isLoading: Boolean,
    onEvent: (MedicationUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val globalForm = forms.firstOrNull() ?: MedicationFormState()
    val isSingleMode = forms.size == 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        MedicationTypeSelector(
            selectedType = globalForm.selectedType,
            onTypeSelected = { onEvent(MedicationUiEvent.UpdateType(index = -1, type = it)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MedicationInfoSection(
            forms = forms,
            onEvent = onEvent
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text =
                if (isSingleMode) stringResource(R.string.medication_setting_alarm)
                else stringResource(R.string.medication_setting_alarm_total),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )

        MedicationAlarmSection(
            medicationIndex = if (isSingleMode) 0 else -1,
            form = globalForm,
            onEvent = onEvent
        )

        Spacer(modifier = Modifier.height(24.dp))

        PharmPrimaryButton(
            text =
                if (!isSingleMode) stringResource(R.string.medication_add_all_format, forms.size)
                else stringResource(R.string.medication_add),
            onClick = { onEvent(MedicationUiEvent.SaveAllMedications) },
            enabled = forms.all { it.medicationName.isNotEmpty() } && !isLoading,
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}