package com.moon.pharm.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
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

//val NavBackStackEntry.topBarAsRouteName: TopBarData
//    get() {
fun NavBackStackEntry.getTopBarData(navController: NavController): TopBarData {
        val routeName = destination.route ?: return TopBarData()
        return when {
            routeName.contains("홈") -> {
                defaultHomeTopBarData()
            }
            routeName.contains("복약관리") -> {
                TopBarData(
                    title = "복약 관리",
                    navigationType = TopBarNavigationType.Back,
                    actions = listOf(
                        TopBarAction(icon = Icons.Filled.Storage, onClick = {}),
                        TopBarAction(icon = Icons.Filled.Add, onClick = {})
                    )
                )
            }
            // "상담글작성"을 "상담"보다 먼저 검사해야 합니다.
            routeName.contains("상담글작성") -> {
                TopBarData(
                    title = "상담글 작성",
                    navigationType = TopBarNavigationType.Close,
                    actions = listOf(
                        TopBarAction(text = "다음", onClick = {})
                    )
                )
            }
            routeName.contains("상담") -> {
                TopBarData(
                    title = "상담 게시판",
                    navigationType = TopBarNavigationType.Back,
                    actions = listOf(
                        TopBarAction(
                            icon = Icons.Filled.Add,
                            onClick = {
                                navController.navigate("상담글작성")
                            }
                        )
                    )
                )
            }
            routeName.contains("내정보") -> {
                TopBarData(
                    title = "내정보"
                )
            }
            else -> throw IllegalArgumentException("내가 선언하지 않은 Route Screen")
        }
    }
