package com.moon.pharm.consult.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.moon.pharm.consult.screen.component.ConsultContent
import com.moon.pharm.consult.viewmodel.ConsultViewModel

@Composable
fun ConsultScreen(
    navController: NavController? = null,
    viewModel: ConsultViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

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
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ConsultContent(
                selectedTab = uiState.selectedTab,
                currentList = if (uiState.selectedTab == ConsultPrimaryTab.LATEST) uiState.consultList else emptyList(),
                onTabSelected = { viewModel.onTabSelected(it) },
                onItemClick = { id ->
                    navController?.navigate(ContentNavigationRoute.ConsultTabDetailScreen(id = id))
                })

            if (uiState.isLoading) {
                CircularProgressBar()
            }
        }
    }
}