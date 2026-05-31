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
import com.moon.pharm.component_ui.component.dialog.PharmConfirmDialog
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.asConsultString
import com.moon.pharm.consult.mapper.toConsultSnackbarType
import com.moon.pharm.consult.screen.component.ConsultDetailContent
import com.moon.pharm.consult.viewmodel.ConsultDetailEffect
import com.moon.pharm.consult.viewmodel.ConsultDetailViewModel

@Composable
fun ConsultDetailScreen(
    navController: NavController,
    consultId: String,
    viewModel: ConsultDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isPharmacistMode = uiState.canAnswer || uiState.isEditingAnswer
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var currentSnackbarType by remember { mutableStateOf(SnackbarType.INFO) }

    var showDeleteConsultDialog by remember { mutableStateOf(false) }
    var showDeleteAnswerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(consultId) {
        if (consultId.isNotBlank()) {
            viewModel.getConsultDetail(consultId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConsultDetailEffect.ShowMessage -> {
                    currentSnackbarType = effect.message.toConsultSnackbarType()
                    snackbarHostState.showSnackbar(effect.message.asConsultString(context))
                }
                is ConsultDetailEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
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
                CustomSnackbar(snackbarData = data, type = currentSnackbarType)
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
                isEditingAnswer = uiState.isEditingAnswer,
                answerInput = uiState.answerContent,
                onAnswerChange = viewModel::onAnswerContentChanged,
                onSubmitAnswer = { viewModel.registerAnswer(consultId) },

                currentUserId = uiState.currentUserId,
                onEditQuestion = {
                    navController.navigate(ContentNavigationRoute.ConsultWriteGraph(consultId = consultId))
                },
                onDeleteQuestion = {
                    showDeleteConsultDialog = true
                },
                onEditAnswer = {
                    viewModel.startEditingAnswer()
                },
                onDeleteAnswer = {
                    showDeleteAnswerDialog = true
                }
            )
            if (showDeleteConsultDialog) {
                PharmConfirmDialog(
                    title = stringResource(R.string.consult_delete_title),
                    content = stringResource(R.string.consult_delete_content),
                    confirmText = stringResource(R.string.consult_delete_confirm),
                    onConfirm = { viewModel.deleteConsult(consultId) },
                    onDismiss = { showDeleteConsultDialog = false }
                )
            }

            if (showDeleteAnswerDialog) {
                PharmConfirmDialog(
                    title = stringResource(R.string.answer_delete_title),
                    content = stringResource(R.string.answer_delete_content),
                    confirmText = stringResource(R.string.consult_delete_confirm),
                    onConfirm = { viewModel.deleteAnswer(consultId) },
                    onDismiss = { showDeleteAnswerDialog = false }
                )
            }
        }
    }
}
