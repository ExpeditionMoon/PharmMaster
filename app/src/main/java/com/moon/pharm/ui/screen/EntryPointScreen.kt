package com.moon.pharm.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.model.BottomBarUiModel
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.view.PharmBottomBar
import com.moon.pharm.component_ui.view.PharmTopBar
import com.moon.pharm.ui.navigation.BottomAppBarItem
import com.moon.pharm.component_ui.view.TopBarData
import com.moon.pharm.component_ui.view.TopBarNavigationType
import com.moon.pharm.consult.navigation.consultNavGraph
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.home.navigation.homeNavGraph
import com.moon.pharm.prescription.navigation.prescriptionNavGraph
import com.moon.pharm.profile.navigation.profileNavGraph
import com.moon.pharm.ui.navigation.getTopBarData

@Composable
fun EntryPointScreen() {
    val navController = rememberNavController()
    val viewModel: ConsultViewModel = hiltViewModel()

    val bottomAppBarItems = remember {
        BottomAppBarItem.fetchBottomAppBarItems()
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val baseTopBarData = navBackStackEntry?.getTopBarData(navController) ?: TopBarData()

    val topBarData = remember(baseTopBarData, navController) {
        when (baseTopBarData.navigationType) {
            TopBarNavigationType.Back, TopBarNavigationType.Close -> {
                baseTopBarData.copy(onNavigationClick = { navController.popBackStack() })
            }
            else -> {
                baseTopBarData
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PharmTopBar(data = topBarData)
        },
        bottomBar = {
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
            PharmBottomBar(
                items = uiModels,
                currentRoute = currentRoute
            )
        },
        floatingActionButton = {
            if (currentRoute?.contains("HomeTab") == true) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(ContentNavigationRoute.PrescriptionCapture ) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    },
                    shape = RoundedCornerShape(100.dp),
                    containerColor = Primary
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.prescription_image),
                        contentDescription = "처방전",
                        modifier = Modifier.size(30.dp),
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = ContentNavigationRoute.HomeTab,
                modifier = Modifier.fillMaxSize()
            ) {
                homeNavGraph(navController)
                consultNavGraph(navController, viewModel = viewModel)
                profileNavGraph(navController)
                prescriptionNavGraph(navController)
            }
        }
    }
}
