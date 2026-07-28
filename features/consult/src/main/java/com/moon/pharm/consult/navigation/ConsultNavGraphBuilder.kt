package com.moon.pharm.consult.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.moon.pharm.consult.screen.ConsultConfirmScreen
import com.moon.pharm.consult.screen.ConsultDetailScreen
import com.moon.pharm.consult.screen.ConsultPharmacistScreen
import com.moon.pharm.consult.screen.ConsultScreen
import com.moon.pharm.consult.screen.ConsultWriteScreen
import com.moon.pharm.consult.screen.my.MyConsultListRoute
import com.moon.pharm.consult.viewmodel.ConsultDetailViewModel
import com.moon.pharm.consult.viewmodel.ConsultListViewModel
import com.moon.pharm.consult.viewmodel.ConsultWriteViewModel

fun NavGraphBuilder.consultNavGraph(
    navController: NavController,
    onMapModeChanged: (Boolean) -> Unit
) {
    composable<MyConsultListRoute> {
        MyConsultListRoute(
            onNavigateUp = { navController.popBackStack() },
            onNavigateToDetail = { consultId ->
                navController.navigate(ConsultDetailRoute(id = consultId))
            }
        )
    }

    navigation<ConsultGraphRoute>(
        startDestination = ConsultRoute
    ) {
        composable<ConsultRoute> {
            val viewModel: ConsultListViewModel = hiltViewModel()
            val refreshFlow = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow(ConsultNavKeys.REFRESH_CONSULT_LIST, false)
            val shouldRefresh by refreshFlow?.collectAsStateWithLifecycle()
                ?: remember { mutableStateOf(false) }

            ConsultScreen(
                viewModel = viewModel,
                shouldRefresh = shouldRefresh,
                onRefreshHandled = {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(ConsultNavKeys.REFRESH_CONSULT_LIST, false)
                },
                onNavigateToWrite = {
                    navController.navigate(ConsultWriteRoute)
                },
                onNavigateToDetail = { consultId ->
                    navController.navigate(ConsultDetailRoute(id = consultId))
                }
            )
        }

        composable<ConsultDetailRoute> { backStackEntry ->
            val detail = backStackEntry.toRoute<ConsultDetailRoute>()
            val viewModel: ConsultDetailViewModel = hiltViewModel()
            ConsultDetailScreen(
                consultId = detail.id,
                viewModel = viewModel,
                onNavigateUp = { navController.popBackStack() },
                onNavigateToEditQuestion = { consultId ->
                    navController.navigate(ConsultWriteGraphRoute(consultId = consultId))
                }
            )
        }

        navigation<ConsultWriteGraphRoute>(
            startDestination = ConsultWriteRoute
        ) {
            consultWriteComposable<ConsultWriteRoute>(navController) { viewModel ->
                ConsultWriteScreen(
                    viewModel = viewModel,
                    onNavigateUp = { navController.popBackStack() },
                    onNavigateToPharmacist = {
                        navController.navigate(ConsultPharmacistRoute)
                    }
                )
            }
            consultWriteComposable<ConsultPharmacistRoute>(navController) { viewModel ->
                ConsultPharmacistScreen(
                    viewModel = viewModel,
                    onMapModeChanged = onMapModeChanged,
                    onNavigateUp = { navController.popBackStack() },
                    onNavigateToConfirm = {
                        navController.navigate(ConsultConfirmRoute)
                    }
                )
            }
            consultWriteComposable<ConsultConfirmRoute>(navController) { viewModel ->
                ConsultConfirmScreen(
                    viewModel = viewModel,
                    onNavigateUp = { navController.popBackStack() },
                    onConsultCreated = {
                        runCatching {
                            navController.getBackStackEntry<ConsultRoute>()
                                .savedStateHandle[ConsultNavKeys.REFRESH_CONSULT_LIST] = true
                        }
                        navController.navigate(ConsultRoute) {
                            popUpTo(ConsultRoute) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onEditTitleOrContent = {
                        navController.popBackStack(ConsultWriteRoute, false)
                    },
                    onEditPharmacist = {
                        navController.popBackStack(
                            ConsultPharmacistRoute,
                            false
                        )
                    }
                )
            }
        }
    }
}

private inline fun <reified T : Any> NavGraphBuilder.consultWriteComposable(
    navController: NavController,
    crossinline content: @Composable (ConsultWriteViewModel) -> Unit
) {
    composable<T> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry<ConsultWriteGraphRoute>()
        }
        val sharedViewModel: ConsultWriteViewModel = hiltViewModel(parentEntry)

        val routeParams = parentEntry.toRoute<ConsultWriteGraphRoute>()
        val editConsultId = routeParams.consultId

        LaunchedEffect(editConsultId) {
            if (editConsultId != null) {
                sharedViewModel.setEditMode(editConsultId)
            }
        }
        content(sharedViewModel)
    }
}
