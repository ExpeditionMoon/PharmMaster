package com.moon.pharm.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.search.screen.SearchMainScreen

fun NavGraphBuilder.searchNavGraph(navController: NavController) {

    composable<ContentNavigationRoute.Search>{
        SearchMainScreen(navController = navController)
    }
}