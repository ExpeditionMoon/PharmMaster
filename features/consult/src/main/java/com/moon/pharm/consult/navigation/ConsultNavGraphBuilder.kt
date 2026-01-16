package com.moon.pharm.consult.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
    viewModel: ConsultViewModel,
    onMapModeChanged: (Boolean) -> Unit
) {

    composable<ContentNavigationRoute.ConsultTab>{
        ConsultScreen(navController = navController, viewModel = viewModel)
    }
    composable<ContentNavigationRoute.ConsultTabWriteScreen>{
        ConsultWriteScreen(navController = navController, viewModel = viewModel)
    }
    composable<ContentNavigationRoute.ConsultTabPharmacistScreen> {
        ConsultPharmacistScreen(navController = navController, viewModel = viewModel, onMapModeChanged = onMapModeChanged)
    }
    composable<ContentNavigationRoute.ConsultTabConfirmScreen> {
        ConsultConfirmScreen(navController = navController, viewModel = viewModel)
    }
    composable<ContentNavigationRoute.ConsultTabDetailScreen> { backStackEntry ->
        val detail : ContentNavigationRoute.ConsultTabDetailScreen = backStackEntry.toRoute()
        ConsultDetailScreen(detail.id, viewModel)
    }
}