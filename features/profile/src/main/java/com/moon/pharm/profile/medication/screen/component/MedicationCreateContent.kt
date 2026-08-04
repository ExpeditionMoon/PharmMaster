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
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.component.button.PharmOutlinedButton
import com.moon.pharm.designsystem.component.button.PharmPrimaryButton
import com.moon.pharm.designsystem.component.card.InfoCardType
import com.moon.pharm.designsystem.component.card.PharmInfoCard
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.section.MedicationAlarmSection
import com.moon.pharm.profile.medication.screen.section.MedicationInfoSection
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

@Composable
fun MedicationCreateContent(
    forms: List<MedicationFormState>,
    sharedMedicationDosage: String,
    isIndividualDosageEditorVisible: Boolean,
    isLoading: Boolean,
    isEditing: Boolean,
    isPrescriptionReview: Boolean,
    onEvent: (MedicationUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val isSingleMedication = forms.size == 1
    val sharedForm = forms.firstOrNull() ?: MedicationFormState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PharmTheme.colors.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        if (isPrescriptionReview) {
            PharmInfoCard(
                message = stringResource(R.string.medication_review_notice),
                type = InfoCardType.NOTICE
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        MedicationTypeSelector(
            selectedType = forms.firstOrNull()?.selectedType ?: MedicationFormState().selectedType,
            onTypeSelected = { onEvent(MedicationUiEvent.UpdateType(index = -1, type = it)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MedicationInfoSection(
            forms = forms,
            sharedMedicationDosage = sharedMedicationDosage,
            isIndividualDosageEditorVisible = isIndividualDosageEditorVisible,
            onEvent = onEvent
        )

        if (!isEditing) {
            Spacer(modifier = Modifier.height(16.dp))

            PharmOutlinedButton(onClick = { onEvent(MedicationUiEvent.AddMedication) }) {
                Text(text = stringResource(R.string.medication_add_another))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        MedicationAlarmSection(
            medicationIndex = if (isSingleMedication) 0 else -1,
            form = sharedForm,
            title = stringResource(
                if (isSingleMedication) {
                    R.string.medication_setting_alarm
                } else {
                    R.string.medication_setting_alarm_total
                }
            ),
            onEvent = onEvent
        )

        Spacer(modifier = Modifier.height(24.dp))

        PharmPrimaryButton(
            text =
                when {
                    isEditing -> stringResource(R.string.medication_save)
                    isPrescriptionReview -> stringResource(R.string.medication_add)
                    forms.size > 1 -> stringResource(R.string.medication_add_all_format, forms.size)
                    else -> stringResource(R.string.medication_add)
                },
            onClick = { onEvent(MedicationUiEvent.SaveAllMedications) },
            enabled = forms.all { it.medicationName.isNotEmpty() } && !isLoading,
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@ThemePreviews
@Composable
private fun MedicationCreateContentPreview() {
    PharmMasterTheme {
        MedicationCreateContent(
            forms = listOf(
                MedicationFormState(
                    medicationName = "타이레놀",
                    medicationDosage = "1알"
                )
            ),
            sharedMedicationDosage = "",
            isIndividualDosageEditorVisible = false,
            isLoading = false,
            isEditing = false,
            isPrescriptionReview = false,
            onEvent = {}
        )
    }
}
