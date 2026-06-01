package com.moon.pharm.consult.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.navigation.ConsultNavKeys.REFRESH_CONSULT_LIST
import com.moon.pharm.consult.screen.ConsultConfirmScreen
import com.moon.pharm.consult.screen.ConsultDetailScreen
import com.moon.pharm.consult.screen.ConsultPharmacistScreen
import com.moon.pharm.consult.screen.ConsultScreen
import com.moon.pharm.consult.screen.ConsultWriteScreen
import com.moon.pharm.consult.viewmodel.ConsultDetailViewModel
import com.moon.pharm.consult.viewmodel.ConsultListViewModel
import com.moon.pharm.consult.viewmodel.ConsultWriteViewModel

fun NavGraphBuilder.consultNavGraph(
    navController: NavController,
    onMapModeChanged: (Boolean) -> Unit
) {
    navigation<ContentNavigationRoute.ConsultGraph>(
        startDestination = ContentNavigationRoute.ConsultTab
    ) {
        composable<ContentNavigationRoute.ConsultTab> {
            val viewModel: ConsultListViewModel = hiltViewModel()
            val shouldRefresh by it.savedStateHandle
                .getStateFlow(REFRESH_CONSULT_LIST, false)
                .collectAsStateWithLifecycle()

            ConsultScreen(
                viewModel = viewModel,
                shouldRefresh = shouldRefresh,
                onRefreshHandled = {
                    it.savedStateHandle[REFRESH_CONSULT_LIST] = false
                },
                onNavigateToWrite = {
                    navController.navigate(ContentNavigationRoute.ConsultTabWriteScreen)
                },
                onNavigateToDetail = { consultId ->
                    navController.navigate(
                        ContentNavigationRoute.ConsultTabDetailScreen(id = consultId)
                    )
                }
            )
        }

        composable<ContentNavigationRoute.ConsultTabDetailScreen> { backStackEntry ->
            val detail = backStackEntry.toRoute<ContentNavigationRoute.ConsultTabDetailScreen>()
            val viewModel: ConsultDetailViewModel = hiltViewModel()
            ConsultDetailScreen(
                consultId = detail.id,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = { consultId ->
                    navController.navigate(ContentNavigationRoute.ConsultWriteGraph(consultId = consultId))
                }
            )
        }

        navigation<ContentNavigationRoute.ConsultWriteGraph>(
            startDestination = ContentNavigationRoute.ConsultTabWriteScreen
        ) {
            consultWriteComposable<ContentNavigationRoute.ConsultTabWriteScreen>(navController) { viewModel ->
                ConsultWriteScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    onNavigateToPharmacist = {
                        navController.navigate(ContentNavigationRoute.ConsultTabPharmacistScreen)
                    }
                )
            }
            consultWriteComposable<ContentNavigationRoute.ConsultTabPharmacistScreen>(navController) { viewModel ->
                ConsultPharmacistScreen(
                    viewModel = viewModel,
                    onMapModeChanged = onMapModeChanged,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToConfirm = {
                        navController.navigate(ContentNavigationRoute.ConsultTabConfirmScreen)
                    }
                )
            }
            consultWriteComposable<ContentNavigationRoute.ConsultTabConfirmScreen>(navController) { viewModel ->
                ConsultConfirmScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onCreateSuccess = {
                        navController.getBackStackEntry(ContentNavigationRoute.ConsultTab)
                            .savedStateHandle[REFRESH_CONSULT_LIST] = true

                        navController.navigate(ContentNavigationRoute.ConsultTab) {
                            popUpTo(ContentNavigationRoute.ConsultTab) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onEditTitleOrContent = {
                        navController.popBackStack(ContentNavigationRoute.ConsultTabWriteScreen, false)
                    },
                    onEditPharmacist = {
                        navController.popBackStack(
                            ContentNavigationRoute.ConsultTabPharmacistScreen,
                            false
                        )
                    }
                )
            }
        }
    }
}

private inline fun <reified T : ContentNavigationRoute> NavGraphBuilder.consultWriteComposable(
    navController: NavController,
    crossinline content: @Composable (ConsultWriteViewModel) -> Unit
) {
    composable<T> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry<ContentNavigationRoute.ConsultWriteGraph>()
        }
        val sharedViewModel: ConsultWriteViewModel = hiltViewModel(parentEntry)

        val routeParams = parentEntry.toRoute<ContentNavigationRoute.ConsultWriteGraph>()
        val editConsultId = routeParams.consultId

        LaunchedEffect(editConsultId) {
            if (editConsultId != null) {
                sharedViewModel.setEditMode(editConsultId)
            }
        }
        content(sharedViewModel)
    }
}
