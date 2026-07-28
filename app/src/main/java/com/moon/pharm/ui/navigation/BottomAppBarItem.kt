package com.moon.pharm.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.moon.pharm.consult.navigation.ConsultRoute
import com.moon.pharm.home.navigation.HomeRoute
import com.moon.pharm.profile.navigation.MedicationRoute
import com.moon.pharm.profile.navigation.ProfileRoute

data class BottomAppBarItem (
    val tabName: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val destination: Any
){
    companion object {
        fun fetchBottomAppBarItems() = listOf(
            BottomAppBarItem(
                tabName = "홈",
                icon = Icons.Filled.Home,
                destination = HomeRoute
            ),
            BottomAppBarItem(
                tabName = "복약관리",
                icon = Icons.Filled.Medication,
                destination = MedicationRoute
            ),
            BottomAppBarItem(
                tabName = "상담",
                icon = Icons.Filled.MarkChatUnread,
                destination = ConsultRoute
            ),
            BottomAppBarItem(
                tabName = "내정보",
                icon = Icons.Filled.Person,
                destination = ProfileRoute
            )
        )
    }
}
