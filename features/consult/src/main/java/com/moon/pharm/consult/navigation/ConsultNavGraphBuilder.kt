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
import com.moon.pharm.consult.viewmodel.ConsultViewModel

fun NavGraphBuilder.consultNavGraph(
    navController: NavController,
    onMapModeChanged: (Boolean) -> Unit
) {
    navigation<ContentNavigationRoute.ConsultGraph>(
        startDestination = ContentNavigationRoute.ConsultTab
    ) {
        consultComposable<ContentNavigationRoute.ConsultTab>(navController) { viewModel, _ ->
            ConsultScreen(navController = navController, viewModel = viewModel)
        }

        consultComposable<ContentNavigationRoute.ConsultTabWriteScreen>(navController) { viewModel, _ ->
            ConsultWriteScreen(navController = navController, viewModel = viewModel)
        }

        consultComposable<ContentNavigationRoute.ConsultTabPharmacistScreen>(navController) { viewModel, _ ->
            ConsultPharmacistScreen(navController = navController, viewModel = viewModel, onMapModeChanged = onMapModeChanged)
        }

        consultComposable<ContentNavigationRoute.ConsultTabConfirmScreen>(navController) { viewModel, _ ->
            ConsultConfirmScreen(navController = navController, viewModel = viewModel)
        }

        consultComposable<ContentNavigationRoute.ConsultTabDetailScreen>(navController) { viewModel, entry ->
            val detail = entry.toRoute<ContentNavigationRoute.ConsultTabDetailScreen>()
            ConsultDetailScreen(navController = navController, consultId = detail.id, viewModel = viewModel)
        }
    }
}

private inline fun <reified T : ContentNavigationRoute> NavGraphBuilder.consultComposable(
    navController: NavController,
    crossinline content: @Composable (ConsultViewModel, androidx.navigation.NavBackStackEntry) -> Unit
) {
    composable<T> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry<ContentNavigationRoute.ConsultGraph>()
        }
        val sharedViewModel: ConsultViewModel = hiltViewModel(parentEntry)
        content(sharedViewModel, backStackEntry)
    }
}