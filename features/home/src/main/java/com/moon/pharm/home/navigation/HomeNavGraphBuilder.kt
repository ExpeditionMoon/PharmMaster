package com.moon.pharm.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.home.screen.HomeMainScreen

fun NavGraphBuilder.homeNavGraph(
    onNavigateToSearch: () -> Unit,
    onNavigateToPrescriptionCapture: () -> Unit
) {

    composable<HomeRoute> {
        HomeMainScreen(
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToPrescriptionCapture = onNavigateToPrescriptionCapture
        )
    }
}
