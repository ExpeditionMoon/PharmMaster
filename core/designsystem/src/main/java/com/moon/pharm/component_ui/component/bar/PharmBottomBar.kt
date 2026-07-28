package com.moon.pharm.component_ui.component.bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.model.BottomBarUiModel
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun PharmBottomBar(
    items: List<BottomBarUiModel>,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    NavigationBar (
        containerColor = PharmTheme.colors.background,
        modifier = modifier.shadow(elevation = 10.dp)
    ){
        items.forEach { item ->
            val isSelected = item.tabName == currentRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = item.onClick,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.tabName
                    )
                },
                label = {
                    Text(text = item.tabName)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PharmTheme.colors.primary,
                    selectedTextColor = PharmTheme.colors.primary,
                    unselectedIconColor = PharmTheme.colors.placeholder,
                    unselectedTextColor = PharmTheme.colors.placeholder,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PharmBottomBarPreview() {
    PharmMasterTheme {
        PharmBottomBar(
            items = listOf(
                BottomBarUiModel(tabName = "홈", icon = Icons.Default.Home, onClick = {}),
                BottomBarUiModel(tabName = "마이페이지", icon = Icons.Default.Person, onClick = {})
            ),
            currentRoute = "home"
        )
    }
}