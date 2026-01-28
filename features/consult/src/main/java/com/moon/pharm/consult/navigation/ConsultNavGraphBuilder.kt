package com.moon.pharm.consult.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
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
            ConsultScreen(navController = navController, viewModel = viewModel)
        }

        composable<ContentNavigationRoute.ConsultTabDetailScreen> { backStackEntry ->
            val detail = backStackEntry.toRoute<ContentNavigationRoute.ConsultTabDetailScreen>()
            val viewModel: ConsultDetailViewModel = hiltViewModel()
            ConsultDetailScreen(navController = navController, consultId = detail.id, viewModel = viewModel)
        }

        navigation<ContentNavigationRoute.ConsultWriteGraph>(
            startDestination = ContentNavigationRoute.ConsultTabWriteScreen
        ) {
            consultWriteComposable<ContentNavigationRoute.ConsultTabWriteScreen>(navController) { viewModel ->
                ConsultWriteScreen(navController = navController, viewModel = viewModel)
            }
            consultWriteComposable<ContentNavigationRoute.ConsultTabPharmacistScreen>(navController) { viewModel ->
                ConsultPharmacistScreen(navController = navController, viewModel = viewModel, onMapModeChanged = onMapModeChanged)
            }
            consultWriteComposable<ContentNavigationRoute.ConsultTabConfirmScreen>(navController) { viewModel ->
                ConsultConfirmScreen(navController = navController, viewModel = viewModel)
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
        content(sharedViewModel)
    }
}