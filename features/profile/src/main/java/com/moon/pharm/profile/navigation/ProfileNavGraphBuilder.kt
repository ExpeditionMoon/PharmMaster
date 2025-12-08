package com.moon.pharm.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.profile.screen.MedicationScreen
import com.moon.pharm.profile.screen.ProfileScreen

fun NavGraphBuilder.profileNavGraph(navController: NavController) {

    composable<ContentNavigationRoute.ProfileTab>{
        ProfileScreen()
    }
    composable<ContentNavigationRoute.MedicationTab>{
        MedicationScreen()
    }
}