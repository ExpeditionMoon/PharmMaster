package com.moon.pharm.profile.medication.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.component.button.PharmOutlinedButton
import com.moon.pharm.designsystem.component.input.PrimaryTextField
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.component.PeriodInputSection
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

@Composable
fun MedicationInfoSection(
    forms: List<MedicationFormState>,
    sharedMedicationDosage: String,
    isIndividualDosageEditorVisible: Boolean,
    onEvent: (MedicationUiEvent) -> Unit
) {
    val isMultipleMedication = forms.size > 1
    val sharedForm = forms.firstOrNull() ?: MedicationFormState()

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(PharmTheme.colors.background)
            .background(PharmTheme.colors.surface.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        if (isMultipleMedication) {
            Text(
                text = stringResource(R.string.medication_detected_count, forms.size),
                style = PharmTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

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

                    IconButton(
                        onClick = { onEvent(MedicationUiEvent.RemoveMedication(index)) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.medication_delete_desc),
                            tint = PharmTheme.colors.placeholder
                        )
                    }
                }

                if (index < forms.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.medication_common_setting_title),
                style = PharmTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            PrimaryTextField(
                value = sharedMedicationDosage,
                onValueChange = { onEvent(MedicationUiEvent.UpdateDosage(index = -1, dosage = it)) },
                placeholder = stringResource(R.string.medication_common_dosage_hint)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PeriodInputSection(
                medicationIndex = -1,
                startDate = sharedForm.startDate,
                endDate = sharedForm.endDate,
                noEndDate = sharedForm.noEndDate,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(12.dp))

            PharmOutlinedButton(
                onClick = { onEvent(MedicationUiEvent.ToggleIndividualDosageEditor) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(
                            if (isIndividualDosageEditorVisible) {
                                R.string.medication_individual_dosage_hide
                            } else {
                                R.string.medication_individual_dosage
                            }
                        )
                    )
                }
            }

            if (isIndividualDosageEditorVisible) {
                forms.forEachIndexed { index, form ->
                    Text(
                        text = form.medicationName.ifBlank {
                            stringResource(R.string.medication_name_hint)
                        },
                        style = PharmTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    PrimaryTextField(
                        value = form.medicationDosage.orEmpty(),
                        onValueChange = { onEvent(MedicationUiEvent.UpdateDosage(index, it)) },
                        placeholder = stringResource(R.string.medication_dosage_hint)
                    )

                    if (form.usesIndividualDosage) {
                        TextButton(
                            onClick = { onEvent(MedicationUiEvent.ToggleIndividualDosage(index)) }
                        ) {
                            Text(text = stringResource(R.string.medication_use_common_dosage))
                        }
                    }

                    if (index < forms.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        } else {
            val form = forms.firstOrNull() ?: MedicationFormState()
            PrimaryTextField(
                value = form.medicationName,
                onValueChange = { onEvent(MedicationUiEvent.UpdateName(name = it)) },
                placeholder = stringResource(R.string.medication_name_hint)
            )

            Spacer(modifier = Modifier.height(10.dp))

            PrimaryTextField(
                value = form.medicationDosage.orEmpty(),
                onValueChange = { onEvent(MedicationUiEvent.UpdateDosage(dosage = it)) },
                placeholder = stringResource(R.string.medication_dosage_hint)
            )

            Spacer(modifier = Modifier.height(10.dp))

            PeriodInputSection(
                medicationIndex = 0,
                startDate = form.startDate,
                endDate = form.endDate,
                noEndDate = form.noEndDate,
                onEvent = onEvent
            )
        }
    }
}

@ThemePreviews
@Composable
private fun MedicationInfoSectionPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MedicationInfoSection(
                forms = listOf(
                    MedicationFormState(medicationName = "타이레놀 500mg"),
                    MedicationFormState(medicationName = "오메가3")
                ),
                sharedMedicationDosage = "",
                isIndividualDosageEditorVisible = false,
                onEvent = {}
            )
        }
    }
}
