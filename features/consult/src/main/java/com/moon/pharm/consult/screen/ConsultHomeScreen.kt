package com.moon.pharm.consult.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.model.TopBarAction
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.util.MultipleEventsCutter
import com.moon.pharm.consult.R
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.consult.navigation.ConsultNavKeys.REFRESH_CONSULT_LIST
import com.moon.pharm.consult.screen.component.ConsultContent
import com.moon.pharm.consult.viewmodel.ConsultListViewModel

@Composable
fun ConsultScreen(
    navController: NavController? = null,
    viewModel: ConsultListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    if (navController != null) {
        val refreshFlow = navController.currentBackStackEntry
                ?.savedStateHandle
            ?.getStateFlow(REFRESH_CONSULT_LIST, false)

        val shouldRefresh by refreshFlow?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) }

        LaunchedEffect(shouldRefresh) {
            if (shouldRefresh) {
                viewModel.fetchConsultList()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(REFRESH_CONSULT_LIST, false)
            }
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.consult_board_title),
                    navigationType = TopBarNavigationType.None,
                    onNavigationClick = { navController?.popBackStack() },
                    actions = listOf(
                        TopBarAction(
                            icon = Icons.Filled.Add,
                            onClick = {
                                multipleEventsCutter.processEvent {
                                    navController?.navigate(ContentNavigationRoute.ConsultTabWriteScreen)
                                }
                            }
                        )
                    )
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ConsultContent(
                selectedTab = uiState.selectedTab,
                currentList = if (uiState.selectedTab == ConsultPrimaryTab.LATEST) uiState.consultList else emptyList(),
                onTabSelected = { viewModel.onTabSelected(it) },
                onItemClick = { id ->
                    navController?.navigate(ContentNavigationRoute.ConsultTabDetailScreen(id = id))
                })

            if (uiState.isLoading) {
                CircularProgressBar(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}