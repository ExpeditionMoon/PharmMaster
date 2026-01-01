package com.moon.pharm.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.profile.medication.screen.MedicationCreateScreen
import com.moon.pharm.profile.medication.screen.MedicationScreen
import com.moon.pharm.profile.mypage.screen.MyPageScreen
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel

fun NavGraphBuilder.profileNavGraph(navController: NavController, viewModel: MedicationViewModel) {

    composable<ContentNavigationRoute.ProfileTab>{
        MyPageScreen()
    }
    composable<ContentNavigationRoute.MedicationTab>{
        MedicationScreen(navController = navController, viewModel = viewModel)
    }
    composable<ContentNavigationRoute.MedicationTabCreateScreen>{
        MedicationCreateScreen(navController = navController, viewModel = viewModel)
    }
}