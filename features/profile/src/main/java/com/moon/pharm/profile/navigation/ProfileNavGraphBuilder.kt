package com.moon.pharm.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.profile.screen.medication.MedicationCreateScreen
import com.moon.pharm.profile.screen.medication.MedicationScreen
import com.moon.pharm.profile.screen.profile.ProfileScreen
import com.moon.pharm.profile.viewmodel.MedicationViewModel

fun NavGraphBuilder.profileNavGraph(navController: NavController, viewModel: MedicationViewModel) {

    composable<ContentNavigationRoute.ProfileTab>{
        ProfileScreen()
    }
    composable<ContentNavigationRoute.MedicationTab>{
        MedicationScreen(navController = navController, viewModel = viewModel)
    }
    composable<ContentNavigationRoute.MedicationTabCreateScreen>{
        MedicationCreateScreen(navController = navController, viewModel = viewModel)
    }
}