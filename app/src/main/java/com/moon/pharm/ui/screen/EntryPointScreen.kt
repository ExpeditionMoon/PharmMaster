package com.moon.pharm.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.profile.navigation.authNavGraph
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EntryPointScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val rootNavController = rememberNavController()
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { route ->
            rootNavController.navigate(route) {
                launchSingleTop = true
            }
        }
    }

    if (startDestination != null) {
        NavHost(
            navController = rootNavController,
            startDestination = startDestination!!
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
}
