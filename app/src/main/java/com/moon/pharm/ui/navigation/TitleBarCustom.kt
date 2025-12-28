package com.moon.pharm.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.view.TopBarAction
import com.moon.pharm.component_ui.view.TopBarData
import com.moon.pharm.component_ui.view.TopBarNavigationType


fun defaultHomeTopBarData() = TopBarData(
    title = "홈",
    navigationType = TopBarNavigationType.Menu,
    isLogoTitle = true,
    actions = listOf(
        TopBarAction(icon = Icons.Filled.Search, onClick = {}),
        TopBarAction(icon = Icons.Filled.Notifications, onClick = {})
    )
)

fun NavBackStackEntry.getTopBarData(navController: NavController): TopBarData {
        val routeName = destination.route ?: return TopBarData()
        return when {
            routeName.contains("HomeTab") -> {
                defaultHomeTopBarData()
            }
            routeName.contains("MedicationTabCreateScreen") -> {
                TopBarData(
                    title = "복약 알림 설정",
                    navigationType = TopBarNavigationType.Close,
                    actions = listOf(
                        TopBarAction(
                            text = "완료",
                            onClick = {
                                navController.navigate(ContentNavigationRoute.MedicationTab)
                            }
                        )
                    )
                )
            }
            routeName.contains("MedicationTab") -> {
                TopBarData(
                    title = "복약 관리",
                    navigationType = TopBarNavigationType.Back,
                    actions = listOf(
                        TopBarAction(icon = Icons.Filled.Storage, onClick = {}),
                        TopBarAction(
                            icon = Icons.Filled.Add,
                            onClick = {
                                navController.navigate(ContentNavigationRoute.MedicationTabCreateScreen)
                            }
                        )
                    )
                )
            }
            routeName.contains("ConsultTabWriteScreen") -> {
                TopBarData(
                    title = "상담글 작성",
                    navigationType = TopBarNavigationType.Close,
                    actions = listOf(
                        TopBarAction(
                            text = "다음",
                            onClick = {
                                navController.navigate(ContentNavigationRoute.ConsultTabPharmacistScreen)
                            }
                        )
                    )
                )
            }
            routeName.contains("ConsultTabPharmacistScreen") -> {
                TopBarData(
                    title = "상담글 작성",
                    navigationType = TopBarNavigationType.Close,
                    actions = listOf(
                        TopBarAction(
                            text = "다음",
                            onClick = {
                                navController.navigate(ContentNavigationRoute.ConsultTabConfirmScreen)
                            }
                        )
                    )
                )
            }
            routeName.contains("ConsultTabConfirmScreen") -> {
                TopBarData(
                    title = "상담글 작성",
                    navigationType = TopBarNavigationType.Close,
                    actions = listOf(
                        TopBarAction(
                            text = "완료",
                            onClick = {
                                navController.navigate(ContentNavigationRoute.ConsultTab)
                            }
                        )
                    )
                )
            }
            routeName.contains("ConsultTabDetailScreen") -> {
                TopBarData(
                    title = "상담 게시판", // 혹은 "상담 상세"
                    navigationType = TopBarNavigationType.Back
                )
            }
            routeName.contains("ConsultTab") -> {
                TopBarData(
                    title = "상담 게시판",
                    navigationType = TopBarNavigationType.Back,
                    actions = listOf(
                        TopBarAction(
                            icon = Icons.Filled.Add,
                            onClick = {
                                navController.navigate(ContentNavigationRoute.ConsultTabWriteScreen)
                            }
                        )
                    )
                )
            }
            routeName.contains("ProfileTab") -> {
                TopBarData(
                    title = "내정보"
                )
            }
            routeName.contains("PrescriptionCapture") -> {
                TopBarData(
                    title = "처방전"
                )
            }

            else -> throw IllegalArgumentException("내가 선언하지 않은 Route Screen")
        }
    }
