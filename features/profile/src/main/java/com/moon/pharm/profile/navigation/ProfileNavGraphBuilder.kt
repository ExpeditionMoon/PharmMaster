package com.moon.pharm.profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.profile.auth.screen.SignUpScreen
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel
import com.moon.pharm.profile.medication.screen.MedicationCreateScreen
import com.moon.pharm.profile.medication.screen.MedicationScreen
import com.moon.pharm.profile.mypage.screen.MyPageScreen
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel

fun NavGraphBuilder.profileNavGraph(navController: NavController) {

    composable<ContentNavigationRoute.SignUpScreen>{
        val viewModel: SignUpViewModel = hiltViewModel()
        SignUpScreen(
            viewModel = viewModel,
            onNavigateToHome = {
                navController.navigate(ContentNavigationRoute.HomeTab) {
                    popUpTo(ContentNavigationRoute.SignUpScreen) { inclusive = true }
                }
            }
        )
    }
    composable<ContentNavigationRoute.ProfileTab>{
        MyPageScreen()
    }
    composable<ContentNavigationRoute.MedicationTab>{
        val viewModel: MedicationViewModel = hiltViewModel()
        MedicationScreen(navController = navController, viewModel)
    }
    composable<ContentNavigationRoute.MedicationTabCreateScreen>{
        val viewModel: MedicationViewModel = hiltViewModel()
        MedicationCreateScreen(navController = navController, viewModel)
    }
}