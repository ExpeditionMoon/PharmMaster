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

fun NavGraphBuilder.consultNavGraph(navController: NavController) {

    composable<ContentNavigationRoute.ConsultTab>{
        ConsultScreen(navController)
    }
    composable<ContentNavigationRoute.ConsultTabWriteScreen>{
        ConsultWriteScreen(navController)
    }
    composable<ContentNavigationRoute.ConsultTabPharmacistScreen> {
        ConsultPharmacistScreen(navController)
    }
    composable<ContentNavigationRoute.ConsultTabConfirmScreen> {
        ConsultConfirmScreen()
    }
    composable<ContentNavigationRoute.ConsultTabDetailScreen> { backStackEntry ->
        val detail : ContentNavigationRoute.ConsultTabDetailScreen = backStackEntry.toRoute()
        ConsultDetailScreen(detail.id)
    }
}