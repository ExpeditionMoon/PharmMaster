package com.moon.pharm.profile.medication.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.component_ui.util.toDisplayString
import com.moon.pharm.component_ui.util.toQueryString
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.component.HistoryRecordItem
import com.moon.pharm.profile.medication.screen.section.HistoryCalendarSection
import com.moon.pharm.profile.medication.viewmodel.MedicationHistoryUiState
import com.moon.pharm.profile.medication.viewmodel.MedicationHistoryViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MedicationHistoryScreen(
    viewModel: MedicationHistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchMonthlyRecords(uiState.selectedMonth)
    }

    MedicationHistoryContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onMonthChanged = { viewModel.fetchMonthlyRecords(it) },
        onDateClick = { viewModel.onDateSelected(it) },
        onToggleRecord = { medId, schId, isTaken ->
            viewModel.toggleRecord(medId, schId, isTaken, uiState.selectedDate)
        },
        onDeleteClick = { medicationId ->
            viewModel.deleteMedication(medicationId)
        }
    )
}

@Composable
fun MedicationHistoryContent(
    uiState: MedicationHistoryUiState,
    onBackClick: () -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    onToggleRecord: (String, String, Boolean) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.medication_history_title),
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = onBackClick
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HistoryCalendarSection(
                currentMonth = uiState.selectedMonth,
                recordsByDate = uiState.recordsByDate,
                selectedDate = uiState.selectedDate,
                onMonthChanged = onMonthChanged,
                onDateClick = onDateClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.medication_history_detail_title, uiState.selectedDate.toDisplayString()),
                modifier = Modifier.padding(horizontal = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = PharmTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            val queryDateKey = uiState.selectedDate.toQueryString()
            val dailyRecords = uiState.recordsByDate[queryDateKey] ?: emptyList()

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (dailyRecords.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.medication_history_empty),
                            modifier = Modifier.padding(top = 20.dp),
                            color = PharmTheme.colors.secondFont
                        )
                    }
                }

                items(dailyRecords) { uiModel ->
                    HistoryRecordItem(
                        uiModel = uiModel,
                        onRecordClick = {
                            onToggleRecord(
                                uiModel.record.medicationId,
                                uiModel.record.scheduleId,
                                !uiModel.record.isTaken
                            )
                        },
                        onDeleteClick = { onDeleteClick(uiModel.record.medicationId) }
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MedicationHistoryContentPreview() {
    PharmMasterTheme {
        MedicationHistoryContent(
            uiState = MedicationHistoryUiState(
                selectedMonth = YearMonth.now(),
                selectedDate = LocalDate.now(),
                recordsByDate = emptyMap()
            ),
            onBackClick = {},
            onMonthChanged = {},
            onDateClick = {},
            onToggleRecord = { _, _, _ -> },
            onDeleteClick = {}
        )
    }
}