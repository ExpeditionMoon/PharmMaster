package com.moon.pharm.component_ui.view

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
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight

@Composable
fun PharmBottomBar(
    items: List<BottomAppBarItem>,
    currentRoute: String?,
    onItemClick: (BottomAppBarItem) -> Unit
) {
    NavigationBar (
        containerColor = backgroundLight,
        modifier = Modifier.shadow(elevation = 10.dp)
    ){
        items.forEach { item ->
            val isSelected = item.tabName == currentRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
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
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent // 선택 시 배경색 제거
                )
            )
        }
    }
}
