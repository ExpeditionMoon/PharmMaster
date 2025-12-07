package com.moon.pharm.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.navigation.PharmNavigation

data class BottomAppBarItem (
    val tabName: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val destination: PharmNavigation
){
    companion object {
        fun fetchBottomAppBarItems() = listOf(
            BottomAppBarItem(
                tabName = "홈",
                icon = Icons.Filled.Home,
                destination = ContentNavigationRoute.HomeTab
            ),
            BottomAppBarItem(
                tabName = "복약관리",
                icon = Icons.Filled.Medication,
                destination = ContentNavigationRoute.MedicationTab
            ),
            BottomAppBarItem(
                tabName = "상담",
                icon = Icons.Filled.MarkChatUnread,
                destination = ContentNavigationRoute.ConsultTab
            ),
            BottomAppBarItem(
                tabName = "내정보",
                icon = Icons.Filled.Person,
                destination = ContentNavigationRoute.ProfileTab
            )
        )
    }
}