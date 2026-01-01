package com.moon.pharm.profile.medication.screen.section

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.component.input.PrimaryTextField
import com.moon.pharm.profile.medication.screen.MedicationForm
import com.moon.pharm.profile.medication.screen.component.MedicationTypeSelector
import com.moon.pharm.profile.medication.screen.component.PeriodInputSection
import com.moon.pharm.profile.medication.viewmodel.MedicationIntent
import com.moon.pharm.profile.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicationInfoSection(
    form: MedicationForm,
    onIntent: (MedicationIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundLight)
            .background(Secondary.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        MedicationTypeSelector(
            selectedType = form.selectedType,
            onTypeSelected = { onIntent(MedicationIntent.UpdateType(it)) }
        )
        Spacer(modifier = Modifier.height(10.dp))

        PrimaryTextField(
            value = form.medicationName,
            onValueChange = { onIntent(MedicationIntent.UpdateName(it)) },
            placeholder = stringResource(R.string.medication_name_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        PrimaryTextField(
            value = form.medicationDosage,
            onValueChange = { onIntent(MedicationIntent.UpdateDosage(it)) },
            placeholder = stringResource(R.string.medication_dosage_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        PeriodInputSection(
            startDate = form.startDate,
            endDate = form.endDate,
            noEndDate = form.noEndDate,
            onIntent = onIntent
        )
    }
}