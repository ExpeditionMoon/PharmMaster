package com.moon.pharm.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moon.pharm.component_ui.component.bar.PharmBottomBar
import com.moon.pharm.component_ui.model.BottomBarUiModel
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.navigation.consultNavGraph
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.home.navigation.homeNavGraph
import com.moon.pharm.prescription.navigation.prescriptionNavGraph
import com.moon.pharm.profile.navigation.profileNavGraph
import com.moon.pharm.ui.navigation.BottomAppBarItem

@Composable
fun EntryPointScreen() {
    val navController = rememberNavController()
    val consultViewModel: ConsultViewModel = hiltViewModel()

    val bottomAppBarItems = remember { BottomAppBarItem.fetchBottomAppBarItems() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isFullScreen = currentRoute?.let { route ->
        route.contains("SignUpScreen") ||
                route.contains("LoginScreen") ||
                route.contains("PrescriptionCapture")
    } == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isFullScreen) {
                val uiModels = bottomAppBarItems.map { navItem ->
                    BottomBarUiModel(
                        tabName = navItem.tabName,
                        icon = navItem.icon,
                        onClick = {
                            navController.navigate(navItem.destination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                PharmBottomBar(items = uiModels, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .consumeWindowInsets(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = ContentNavigationRoute.LoginScreen,
                modifier = Modifier.fillMaxSize()
            ) {
                homeNavGraph(navController)
                consultNavGraph(navController, viewModel = consultViewModel, onMapModeChanged = {})
                profileNavGraph(navController)
                prescriptionNavGraph(navController)
            }
        }
    }
}
