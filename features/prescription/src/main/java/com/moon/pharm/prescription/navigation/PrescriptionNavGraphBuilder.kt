package com.moon.pharm.prescription.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.prescription.screen.PrescriptionScreen

fun NavGraphBuilder.prescriptionNavGraph(
    onNavigateToMedicationCreate: (List<String>) -> Unit
) {

    composable<PrescriptionCaptureRoute> {
        PrescriptionScreen(
            onNavigateToMedicationCreate = onNavigateToMedicationCreate
        )
    }
}
