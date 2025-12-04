package com.moon.pharm.component_ui.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry

data class TopBarData(
    val title: String = "",
    val isLogoTitle: Boolean = false,
    val navigationType: TopBarNavigationType = TopBarNavigationType.None,
    val onNavigationClick: () -> Unit = {},
    val actions: List<TopBarAction> = emptyList()
)

enum class TopBarNavigationType {
    None, Back, Menu, Close // Close(X버튼) 추가
}

data class TopBarAction(
    val icon: ImageVector? = null,
    val text: String? = null,
    val onClick: () -> Unit = {}
)

fun defaultHomeTopBarData() = TopBarData(
    isLogoTitle = true,
    navigationType = TopBarNavigationType.Menu,
    actions = listOf(
        TopBarAction(icon = Icons.Filled.Search, onClick = {}),
        TopBarAction(icon = Icons.Filled.Notifications, onClick = {})
    )
)

val NavBackStackEntry.topBarAsRouteName: TopBarData
    get() {
        val routeName = destination.route ?: return TopBarData()
        return when (routeName) {
            "홈" -> defaultHomeTopBarData()
            "복약관리" -> TopBarData(
                title = "복약 관리",
                navigationType = TopBarNavigationType.Back,
                actions = listOf(
                    TopBarAction(text = "+ 약물 추가", onClick = {})
                )
            )
            "상담" -> TopBarData(
                title = "상담 게시판",
                navigationType = TopBarNavigationType.Back,
                actions = listOf(
                    TopBarAction(icon = Icons.Filled.Add, onClick = {})
                )
            )
            "내정보" -> TopBarData(
                title = "마이페이지",
                navigationType = TopBarNavigationType.Back
            )
            "상담글작성" -> TopBarData(
                title = "상담글 작성",
                navigationType = TopBarNavigationType.Close, // X 버튼
                actions = listOf(
                    TopBarAction(text = "다음", onClick = {})
                )
            )
            else -> TopBarData(title = "PharmMaster")
        }
    }
