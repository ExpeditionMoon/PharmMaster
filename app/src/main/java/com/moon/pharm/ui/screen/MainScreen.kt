package com.moon.pharm.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moon.pharm.component_ui.component.bar.PharmBottomBar
import com.moon.pharm.component_ui.model.BottomBarUiModel
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.consult.navigation.consultNavGraph
import com.moon.pharm.home.navigation.homeNavGraph
import com.moon.pharm.prescription.navigation.prescriptionNavGraph
import com.moon.pharm.profile.navigation.profileNavGraph
import com.moon.pharm.ui.navigation.BottomAppBarItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLogout: () -> Unit
) {
    val mainNavController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { route ->
            if (route == "MedicationScreen") {
                mainNavController.navigate(ContentNavigationRoute.MedicationTab) {
                    popUpTo(mainNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    val bottomAppBarItems = remember { BottomAppBarItem.fetchBottomAppBarItems() }
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isFullScreen = currentRoute?.contains("PrescriptionCapture") == true

    var isMapMode by remember { mutableStateOf(false) }
    val shouldShowBars = !isFullScreen && !isMapMode

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            if (shouldShowBars) {
                val uiModels = bottomAppBarItems.map { navItem ->
                    BottomBarUiModel(
                        tabName = navItem.tabName,
                        icon = navItem.icon,
                        onClick = {
                            mainNavController.navigate(navItem.destination) {
                                popUpTo(mainNavController.graph.findStartDestination().id) {
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
                navController = mainNavController,
                startDestination = ContentNavigationRoute.HomeTab,
                modifier = Modifier.fillMaxSize()
            ) {
                homeNavGraph(mainNavController)
                consultNavGraph(mainNavController, onMapModeChanged = { isMapVisible -> isMapMode = isMapVisible })
                profileNavGraph(mainNavController, onLogout = onLogout)
                prescriptionNavGraph(mainNavController)
            }
        }
    }
}