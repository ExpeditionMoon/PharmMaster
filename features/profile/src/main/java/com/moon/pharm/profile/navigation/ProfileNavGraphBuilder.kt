package com.moon.pharm.profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.model.ScannedMedication
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.screen.my.MyConsultListRoute
import com.moon.pharm.profile.auth.screen.LoginScreen
import com.moon.pharm.profile.auth.screen.SignUpScreen
import com.moon.pharm.profile.auth.viewmodel.LoginViewModel
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel
import com.moon.pharm.profile.medication.screen.MedicationCreateScreen
import com.moon.pharm.profile.medication.screen.MedicationHistoryScreen
import com.moon.pharm.profile.medication.screen.MedicationScreen
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel
import com.moon.pharm.profile.mypage.screen.MyPageRoute
import kotlin.reflect.typeOf

fun NavGraphBuilder.authNavGraph(rootNavController: NavController) {
    composable<ContentNavigationRoute.LoginScreen> {
        val viewModel: LoginViewModel = hiltViewModel()
        LoginScreen(
            viewModel = viewModel,
            onNavigateToSignUp = {
                rootNavController.navigate(ContentNavigationRoute.SignUpScreen)
            },
            onNavigateToHome = {
                rootNavController.navigate(ContentNavigationRoute.MainBase) {
                    popUpTo(ContentNavigationRoute.LoginScreen) { inclusive = true }
                }
            }
        )
    }

    composable<ContentNavigationRoute.SignUpScreen> {
        val viewModel: SignUpViewModel = hiltViewModel()
        SignUpScreen(
            viewModel = viewModel,
            onNavigateToHome = {
                rootNavController.navigate(ContentNavigationRoute.MainBase) {
                    popUpTo(ContentNavigationRoute.SignUpScreen) { inclusive = true }
                }
            }
        )
    }
}

fun NavGraphBuilder.profileNavGraph(navController: NavController, onLogout: () -> Unit) {
    composable<ContentNavigationRoute.ProfileTab> {
        MyPageRoute(
            onNavigateToMyConsultation = {
                navController.navigate(ContentNavigationRoute.MyConsultList)
            },
            onNavigateToMedicationHistory = {
                navController.navigate(ContentNavigationRoute.MedicationTabHistoryScreen)
            },
            onNavigateToLogin = {
                onLogout()
            }
        )
    }
    composable<ContentNavigationRoute.MyConsultList> {
        MyConsultListRoute(
            onNavigateUp = { navController.popBackStack() },
            onNavigateToDetail = { consultId ->
                navController.navigate(ContentNavigationRoute.ConsultTabDetailScreen(id = consultId))
            }
        )
    }

    composable<ContentNavigationRoute.MedicationTab>{
        val viewModel: MedicationViewModel = hiltViewModel()
        MedicationScreen(navController = navController, viewModel)
    }
    composable<ContentNavigationRoute.MedicationTabCreateScreen>(
        typeMap = mapOf(
            typeOf<List<ScannedMedication>>() to ScannedMedicationListNavType
        )
    ){
        val viewModel: MedicationViewModel = hiltViewModel()
        MedicationCreateScreen(navController = navController, viewModel)
    }
    composable<ContentNavigationRoute.MedicationTabHistoryScreen>{
        MedicationHistoryScreen(onBackClick = { navController.popBackStack() })
    }
}