package com.moon.pharm.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moon.pharm.profile.navigation.LoginRoute
import com.moon.pharm.profile.navigation.authNavGraph
import com.moon.pharm.ui.navigation.MainRoute

@Composable
fun EntryPointScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val rootNavController = rememberNavController()
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    startDestination?.let { destination ->
        NavHost(
            navController = rootNavController,
            startDestination = when (destination) {
                AppStartDestination.Main -> MainRoute
                AppStartDestination.Login -> LoginRoute
            }
        ) {
            authNavGraph(rootNavController) {
                rootNavController.navigate(MainRoute) {
                    popUpTo(LoginRoute) { inclusive = true }
                }
            }
            composable<MainRoute> {
                MainScreen(
                    viewModel = viewModel,
                    onLogout = {
                        rootNavController.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
