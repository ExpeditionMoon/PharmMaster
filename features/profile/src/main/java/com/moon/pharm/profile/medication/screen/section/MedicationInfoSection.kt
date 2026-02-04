package com.moon.pharm.profile.medication.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.input.PrimaryTextField
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.component.PeriodInputSection
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

@Composable
fun MedicationInfoSection(
    forms: List<MedicationFormState>,
    onEvent: (MedicationUiEvent) -> Unit
) {
    val isSingleMode = forms.size == 1
    val globalForm = forms.firstOrNull() ?: MedicationFormState()

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundLight)
            .background(Secondary.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        if (!isSingleMode) {
            PeriodInputSection(
                medicationIndex = -1,
                startDate = globalForm.startDate,
                endDate = globalForm.endDate,
                noEndDate = globalForm.noEndDate,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
        }

        forms.forEachIndexed { index, form ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PrimaryTextField(
                    value = form.medicationName,
                    onValueChange = { onEvent(MedicationUiEvent.UpdateName(index = index, name = it)) },
                    placeholder = stringResource(R.string.medication_name_hint),
                    modifier = Modifier.weight(1f)
                )

                if (!isSingleMode) {
                    IconButton(
                        onClick = { onEvent(MedicationUiEvent.RemoveMedication(index)) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "삭제",
                            tint = Color.Gray
                        )
                    }
                }
            }

            if (isSingleMode) {
                Spacer(modifier = Modifier.height(10.dp))

                PrimaryTextField(
                    value = form.medicationDosage.orEmpty(),
                    onValueChange = { onEvent(MedicationUiEvent.UpdateDosage(index = index, dosage = it)) },
                    placeholder = stringResource(R.string.medication_dosage_hint)
                )

                Spacer(modifier = Modifier.height(10.dp))

                PeriodInputSection(
                    medicationIndex = index,
                    startDate = form.startDate,
                    endDate = form.endDate,
                    noEndDate = form.noEndDate,
                    onEvent = onEvent
                )
            }

            if (!isSingleMode && index < forms.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (!isSingleMode) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            PrimaryTextField(
                value = globalForm.medicationDosage.orEmpty(),
                onValueChange = { onEvent(MedicationUiEvent.UpdateDosage(index = -1, dosage = it)) },
                placeholder = stringResource(R.string.medication_dosage_hint)
            )
        }
    }
}