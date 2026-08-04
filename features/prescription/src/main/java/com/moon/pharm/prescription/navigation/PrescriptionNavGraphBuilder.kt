package com.moon.pharm.prescription.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.prescription.screen.PrescriptionScreen

fun NavGraphBuilder.prescriptionNavGraph(
    onNavigateToMedicationReview: (List<String>, Boolean) -> Unit
) {

    composable<PrescriptionCaptureRoute> {
        PrescriptionScreen(
            onNavigateToMedicationReview = onNavigateToMedicationReview
        )
    }
}
