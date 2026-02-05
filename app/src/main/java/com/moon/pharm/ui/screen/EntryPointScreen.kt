package com.moon.pharm.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.profile.navigation.authNavGraph

@Composable
fun EntryPointScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = ContentNavigationRoute.LoginScreen
    ) {
        authNavGraph(rootNavController)
        composable<ContentNavigationRoute.MainBase> {
            MainScreen(
                viewModel = viewModel,
                onLogout = {
                    rootNavController.navigate(ContentNavigationRoute.LoginScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
