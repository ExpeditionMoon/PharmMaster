package com.moon.pharm.consult.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.asString
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.consult.screen.component.ConsultDetailContent
import com.moon.pharm.consult.viewmodel.ConsultDetailViewModel

@Composable
fun ConsultDetailScreen(
    navController: NavController,
    consultId: String,
    viewModel: ConsultDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val answerContent by viewModel.answerContent.collectAsStateWithLifecycle()
    val isPharmacistMode = uiState.canAnswer
    val snackbarHostState = remember { SnackbarHostState() }

    val userMessage = uiState.userMessage
    val messageText = (userMessage as? ConsultUiMessage)?.asString()

    LaunchedEffect(consultId) {
        if (consultId.isNotBlank()) {
            viewModel.getConsultDetail(consultId)
        }
    }

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.consult_board_title),
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = { navController.popBackStack() }
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ConsultDetailContent(
                isLoading = uiState.isLoading,
                item = uiState.selectedItem,
                pharmacist = uiState.answerPharmacist,
                pharmacistImageUrl = uiState.answerPharmacistProfileUrl,

                isPharmacistMode = isPharmacistMode,
                answerInput = answerContent,
                onAnswerChange = viewModel::onAnswerContentChanged,
                onSubmitAnswer = { viewModel.registerAnswer(consultId)}
            )
        }
    }
}