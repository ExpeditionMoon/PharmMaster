package com.moon.pharm.profile.medication.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.designsystem.component.bar.PharmTopBar
import com.moon.pharm.designsystem.model.TopBarAction
import com.moon.pharm.designsystem.model.TopBarData
import com.moon.pharm.designsystem.model.TopBarNavigationType
import com.moon.pharm.designsystem.util.MultipleEventsCutter
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.mapper.asString
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import com.moon.pharm.profile.medication.screen.component.MedicationHomeContent
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel

@Composable
fun MedicationScreen(
    viewModel: MedicationViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val groupedList by viewModel.groupedMedications.collectAsStateWithLifecycle()

    val totalCount = uiState.medicationList.size
    val completedCount = uiState.medicationList.count { it.isTaken }
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = uiState.userMessage as? MedicationUiMessage
    val userMessageText = userMessage?.asString()

    LaunchedEffect(userMessageText) {
        userMessageText?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(MedicationUiEvent.MessageShown)
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.medication_board_title),
                    navigationType = TopBarNavigationType.None,
                    onNavigationClick = {},

                    actions = listOf(
                        TopBarAction(
                            icon = Icons.Default.Storage,
                            onClick = {
                                multipleEventsCutter.processEvent {
                                    onNavigateToHistory()
                                }
                            }
                        ),
                        TopBarAction(
                            icon = Icons.Default.Add,
                            onClick = {
                                multipleEventsCutter.processEvent {
                                    onNavigateToCreate()
                                }
                            }
                        )
                    )
                )
            )
        }
        ,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MedicationHomeContent(
                selectedTab = uiState.selectedTab,
                currentList = groupedList,
                totalCount = totalCount,
                completedCount = completedCount,
                onTabSelected = { viewModel.onTabSelected(it) },
                onTakeClick = { item ->
                    viewModel.onEvent(
                        MedicationUiEvent.ToggleTaken(
                            medicationId = item.medicationId,
                            scheduleId = item.scheduleId
                        )
                    )
                },
                onEditClick = onNavigateToEdit,
                onPauseClick = { medicationId ->
                    viewModel.onEvent(MedicationUiEvent.PauseMedication(medicationId))
                },
                onResumeClick = { medicationId ->
                    viewModel.onEvent(MedicationUiEvent.ResumeMedication(medicationId))
                },
                onEndClick = { medicationId ->
                    viewModel.onEvent(MedicationUiEvent.EndMedication(medicationId))
                }
            )
        }
    }
}
