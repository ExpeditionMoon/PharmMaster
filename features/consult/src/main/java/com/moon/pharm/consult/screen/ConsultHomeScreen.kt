package com.moon.pharm.consult.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarAction
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.util.MultipleEventsCutter
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.asConsultString
import com.moon.pharm.consult.screen.component.ConsultContent
import com.moon.pharm.consult.viewmodel.ConsultListEffect
import com.moon.pharm.consult.viewmodel.ConsultListViewModel

@Composable
fun ConsultScreen(
    viewModel: ConsultListViewModel,
    shouldRefresh: Boolean,
    onRefreshHandled: () -> Unit,
    onNavigateToWrite: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConsultListEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message.asConsultString(context))
                }
            }
        }
    }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.fetchConsultList()
            onRefreshHandled()
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.consult_board_title),
                    navigationType = TopBarNavigationType.None,
                    onNavigationClick = {},
                    actions = listOf(
                        TopBarAction(
                            icon = Icons.Filled.Add,
                            onClick = {
                                multipleEventsCutter.processEvent {
                                    onNavigateToWrite()
                                }
                            }
                        )
                    )
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ConsultContent(
                selectedTab = uiState.selectedTab,
                currentList = uiState.consultList,
                currentUserId = uiState.currentUserId,
                isPharmacist = uiState.isPharmacist,
                onTabSelected = { viewModel.onTabSelected(it) },
                onItemClick = { consultItem ->
                    val isOwner = consultItem.userId == uiState.currentUserId
                    val isPharmacist = uiState.isPharmacist
                    val hasPermission = consultItem.isPublic || isOwner || isPharmacist

                    if (hasPermission) {
                        onNavigateToDetail(consultItem.id)
                    } else {
                        Toast.makeText(context, "작성자만 확인할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            if (uiState.isLoading) {
                CircularProgressBar(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
