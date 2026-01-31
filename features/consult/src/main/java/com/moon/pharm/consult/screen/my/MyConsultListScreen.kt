package com.moon.pharm.consult.screen.my

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.common.asString
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.consult.screen.component.MyConsultEmptyView
import com.moon.pharm.consult.screen.component.MyConsultListContent
import com.moon.pharm.consult.util.myConsultListEmptyTextRes
import com.moon.pharm.consult.util.myConsultListTitleRes
import com.moon.pharm.consult.viewmodel.MyConsultListUiState
import com.moon.pharm.consult.viewmodel.MyConsultListViewModel
import com.moon.pharm.domain.model.auth.UserType

@Composable
fun MyConsultListRoute(
    onNavigateUp: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: MyConsultListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = uiState.userMessage
    val messageText = userMessage?.asString()

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    MyConsultListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateUp = onNavigateUp,
        onItemClick = onNavigateToDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyConsultListScreen(
    uiState: MyConsultListUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val userType = if (uiState.isPharmacist) UserType.PHARMACIST else UserType.GENERAL

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(userType.myConsultListTitleRes),
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = onNavigateUp
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressBar(modifier = Modifier.align(Alignment.Center))
                }
                uiState.myConsults.isEmpty() -> {
                    MyConsultEmptyView(
                        text = stringResource(userType.myConsultListEmptyTextRes),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    MyConsultListContent(
                        items = uiState.myConsults,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}