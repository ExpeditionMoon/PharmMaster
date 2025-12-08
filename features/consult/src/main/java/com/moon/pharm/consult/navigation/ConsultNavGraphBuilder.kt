package com.moon.pharm.consult.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.screen.ConsultScreen
import com.moon.pharm.consult.screen.ConsultWriteScreen

fun NavGraphBuilder.consultNavGraph(navController: NavController) {

    composable<ContentNavigationRoute.ConsultTab>{
        ConsultScreen(navController)
    }
    composable("상담글작성"){
        ConsultWriteScreen(navController)
    }
}