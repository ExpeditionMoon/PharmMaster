package com.moon.pharm.profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.profile.auth.screen.LoginScreen
import com.moon.pharm.profile.auth.screen.SignUpScreen
import com.moon.pharm.profile.auth.viewmodel.LoginViewModel
import com.moon.pharm.profile.auth.viewmodel.SignUpViewModel
import com.moon.pharm.profile.medication.screen.MedicationCreateScreen
import com.moon.pharm.profile.medication.screen.MedicationHistoryScreen
import com.moon.pharm.profile.medication.screen.MedicationScreen
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel
import com.moon.pharm.profile.mypage.screen.MyPageRoute

fun NavGraphBuilder.authNavGraph(rootNavController: NavController, onAuthenticated: () -> Unit) {
    composable<LoginRoute> {
        val viewModel: LoginViewModel = hiltViewModel()
        LoginScreen(
            viewModel = viewModel,
            onNavigateToSignUp = {
                rootNavController.navigate(SignUpRoute)
            },
            onNavigateToHome = {
                onAuthenticated()
            }
        )
    }

    composable<SignUpRoute> {
        val viewModel: SignUpViewModel = hiltViewModel()
        SignUpScreen(
            viewModel = viewModel,
            onNavigateToHome = {
                onAuthenticated()
            }
        )
    }
}

fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    onLogout: () -> Unit,
    onNavigateToMyConsultation: () -> Unit
) {
    composable<ProfileRoute> {
        MyPageRoute(
            onNavigateToMyConsultation = onNavigateToMyConsultation,
            onNavigateToMedicationHistory = {
                navController.navigate(MedicationHistoryRoute)
            },
            onNavigateToLogin = {
                onLogout()
            }
        )
    }
    composable<MedicationRoute> {
        val viewModel: MedicationViewModel = hiltViewModel()
        MedicationScreen(
            viewModel = viewModel,
            onNavigateToHistory = {
                navController.navigate(MedicationHistoryRoute)
            },
            onNavigateToCreate = {
                navController.navigate(MedicationCreateRoute())
            },
            onNavigateToEdit = { medicationId ->
                navController.navigate(MedicationCreateRoute(medicationId = medicationId))
            }
        )
    }
    composable<MedicationCreateRoute> {
        val viewModel: MedicationViewModel = hiltViewModel()
        MedicationCreateScreen(
            viewModel = viewModel,
            onNavigateUp = { navController.popBackStack() },
            onMedicationCreated = {
                navController.navigate(MedicationRoute) {
                    popUpTo(MedicationRoute) { inclusive = false }
                    launchSingleTop = true
                }
            }
        )
    }
    composable<MedicationHistoryRoute> {
        MedicationHistoryScreen(onBackClick = { navController.popBackStack() })
    }
}
