package com.moon.pharm.component_ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.OnSurface
import com.moon.pharm.component_ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmTopBar(
    data: TopBarData
) {
    val navigationIcon: @Composable () -> Unit = {
        when (data.navigationType) {
            TopBarNavigationType.Back -> {
                IconButton(
                    onClick = data.onNavigationClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Primary
                    )
                }
            }
            TopBarNavigationType.Menu -> {
                IconButton(onClick = data.onNavigationClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = Primary
                    )
                }
            }
            TopBarNavigationType.None -> {
                // 아무것도 표시하지 않음
            }
        }
    }

    val actions: @Composable RowScope.() -> Unit = {
        data.actions.forEach { action ->
            if (action.icon != null) {
                IconButton(onClick = action.onClick) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.text,
                        tint = Primary
                    )
                }
            } else if (action.text != null) {
                // 텍스트 버튼 형태의 액션 (예: +약물 추가, +글쓰기)
                TextButton(onClick = action.onClick) {
                    Text(
                        text = action.text,
                        color = OnSurface
                    )
                }
            }
        }
    }

    if (data.isLogoTitle) {
        CenterAlignedTopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo_image),
                    contentDescription = "Pharm Master Logo",
                    modifier = Modifier.height(60.dp)
                )
            },
            navigationIcon = navigationIcon,
            actions = actions,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = data.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            },
            navigationIcon = navigationIcon,
            actions = actions,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
