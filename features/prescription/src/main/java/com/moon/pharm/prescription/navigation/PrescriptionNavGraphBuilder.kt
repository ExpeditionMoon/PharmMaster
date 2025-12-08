package com.moon.pharm.prescription.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.prescription.screen.PrescriptionScreen

fun NavGraphBuilder.prescriptionNavGraph(navController: NavController) {

    composable<ContentNavigationRoute.PrescriptionCapture>{
        PrescriptionScreen()
    }
}