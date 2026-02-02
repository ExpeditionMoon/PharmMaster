package com.moon.pharm.ui.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.moon.pharm.component_ui.model.TopBarData

fun NavBackStackEntry.getTopBarData(navController: NavController): TopBarData {
        val routeName = destination.route ?: return TopBarData()
        return when {
            routeName.contains("PrescriptionCapture") -> {
                TopBarData(
                    title = "처방전"
                )
            }
            else -> throw IllegalArgumentException("내가 선언하지 않은 Route Screen")
        }
    }
