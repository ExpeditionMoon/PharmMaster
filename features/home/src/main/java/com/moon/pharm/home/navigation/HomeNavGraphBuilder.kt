package com.moon.pharm.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.home.screen.HomeMainScreen

fun NavGraphBuilder.homeNavGraph(navController: NavController) {

    composable<ContentNavigationRoute.HomeTab>{
        HomeMainScreen(navController = navController)
    }
}