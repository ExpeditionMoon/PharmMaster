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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.asString
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.consult.screen.component.ConsultConfirmContent
import com.moon.pharm.consult.viewmodel.ConsultWriteViewModel
import com.moon.pharm.designsystem.component.bar.PharmTopBar
import com.moon.pharm.designsystem.component.snackbar.CustomSnackbar
import com.moon.pharm.designsystem.component.snackbar.SnackbarType
import com.moon.pharm.designsystem.model.TopBarData
import com.moon.pharm.designsystem.model.TopBarNavigationType

@Composable
fun ConsultConfirmScreen(
    viewModel: ConsultWriteViewModel,
    onNavigateUp: () -> Unit,
    onConsultCreated: () -> Unit,
    onEditTitleOrContent: () -> Unit,
    onEditPharmacist: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var currentSnackbarType by remember { mutableStateOf(SnackbarType.INFO) }
    val userMessage = uiState.userMessage
    val messageText = (userMessage as? ConsultUiMessage)?.asString()

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            currentSnackbarType = when (userMessage) {
                is ConsultUiMessage.AnswerRegisterSuccess,
                is ConsultUiMessage.ConsultDeleteSuccess,
                is ConsultUiMessage.AnswerDeleteSuccess -> SnackbarType.INFO
                else -> SnackbarType.ERROR
            }
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    val selectedPharmacist = remember(uiState.selectedPharmacistId, uiState.availablePharmacists) {
        uiState.availablePharmacists.find { it.userId == uiState.selectedPharmacistId }
    }

    LaunchedEffect(uiState.isConsultCreated) {
        if (uiState.isConsultCreated) {
            viewModel.resetConsultState()
            onConsultCreated()
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.consult_write_title),
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = onNavigateUp
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
                onEditTitleOrContent = onEditTitleOrContent,
                onEditPharmacist = onEditPharmacist,
                onSubmit = {
                    viewModel.submitConsult()
                }
            )
        }
    }
}
