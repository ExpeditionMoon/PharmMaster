package com.moon.pharm.profile.medication.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.model.TopBarAction
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.util.MultipleEventsCutter
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.component.MedicationHomeContent
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel

@Composable
fun MedicationScreen(
    navController: NavController? = null, viewModel: MedicationViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val groupedList by viewModel.groupedMedications.collectAsStateWithLifecycle()

    val totalCount = uiState.medicationList.size
    val completedCount = uiState.medicationList.count { it.isTaken }
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

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
                                    navController?.navigate(ContentNavigationRoute.MedicationTabHistoryScreen)
                                }
                            }
                        ),
                        TopBarAction(
                            icon = Icons.Default.Add,
                            onClick = {
                                multipleEventsCutter.processEvent {
                                    navController?.navigate(ContentNavigationRoute.MedicationTabCreateScreen())
                                }
                            }
                        )
                    )
                )
            )
        }
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
                }
            )
        }
    }
}
