package com.moon.pharm.consult.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.R
import com.moon.pharm.consult.screen.component.ConsultConfirmContent
import com.moon.pharm.consult.viewmodel.ConsultViewModel

@Composable
fun ConsultConfirmScreen(
    navController: NavController,
    viewModel: ConsultViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val writeState = uiState.writeState

    val selectedPharmacist = remember(writeState.selectedPharmacistId, writeState.availablePharmacists) {
        writeState.availablePharmacists.find { it.userId == writeState.selectedPharmacistId }
    }

    LaunchedEffect(uiState.isConsultCreated) {
        if (uiState.isConsultCreated) {
            viewModel.resetConsultState()
            navController.navigate(ContentNavigationRoute.ConsultTab) {
                popUpTo(ContentNavigationRoute.ConsultTab) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.consult_write_title),
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = { navController.popBackStack() }
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ConsultConfirmContent(
                title = writeState.title,
                content = writeState.content,
                selectedPharmacistName = selectedPharmacist?.name ?: stringResource(R.string.consult_confirm_no_pharmacist),
                onEditTitleOrContent = {
                    navController.popBackStack(ContentNavigationRoute.ConsultTabWriteScreen, false)
                },
                onEditPharmacist = {
                    navController.popBackStack(
                        ContentNavigationRoute.ConsultTabPharmacistScreen,
                        false
                    )
                },
                onSubmit = {
                    viewModel.submitConsult()
                }
            )
        }
    }
}
