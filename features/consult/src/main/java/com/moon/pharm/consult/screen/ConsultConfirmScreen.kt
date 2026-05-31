package com.moon.pharm.consult.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.asConsultString
import com.moon.pharm.consult.mapper.toConsultSnackbarType
import com.moon.pharm.consult.navigation.ConsultNavKeys.REFRESH_CONSULT_LIST
import com.moon.pharm.consult.screen.component.ConsultConfirmContent
import com.moon.pharm.consult.viewmodel.ConsultWriteEffect
import com.moon.pharm.consult.viewmodel.ConsultWriteViewModel

@Composable
fun ConsultConfirmScreen(
    navController: NavController,
    viewModel: ConsultWriteViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var currentSnackbarType by remember { mutableStateOf(SnackbarType.INFO) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConsultWriteEffect.ShowMessage -> {
                    currentSnackbarType = effect.message.toConsultSnackbarType()
                    snackbarHostState.showSnackbar(effect.message.asConsultString(context))
                }
                is ConsultWriteEffect.CreateSuccess -> {
                    try {
                        navController.getBackStackEntry(ContentNavigationRoute.ConsultTab)
                            .savedStateHandle[REFRESH_CONSULT_LIST] = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    navController.navigate(ContentNavigationRoute.ConsultTab) {
                        popUpTo(ContentNavigationRoute.ConsultTab) { inclusive = false }
                        launchSingleTop = true
                    }
                }
                is ConsultWriteEffect.MoveCamera,
                is ConsultWriteEffect.MoveToPharmacist,
                is ConsultWriteEffect.UpdateSuccess -> Unit
            }
        }
    }

    val selectedPharmacist = remember(uiState.selectedPharmacistId, uiState.availablePharmacists) {
        uiState.availablePharmacists.find { it.userId == uiState.selectedPharmacistId }
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = currentSnackbarType)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ConsultConfirmContent(
                title = uiState.title,
                content = uiState.content,
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
