package com.moon.pharm.component_ui.component.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.model.TopBarAction
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.theme.OnSurface
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.backgroundLight

/**
 * 앱 표준 상단바.
 * ([TopBarData]를 통해 UI 구성)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmTopBar(
    data: TopBarData,
    modifier: Modifier = Modifier
) {
    val titleContent: @Composable () -> Unit = if (data.isLogoTitle) {
        { PharmLogoTitle() }
    } else {
        { PharmTextTitle(title = data.title) }
    }

    CenterAlignedTopAppBar(
        title = titleContent,
        navigationIcon = {
            PharmNavigationIcon(
                type = data.navigationType,
                onClick = data.onNavigationClick
            )
        },
        actions = {
            PharmActions(actions = data.actions)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundLight
        ),
        windowInsets = WindowInsets.statusBars,
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 5.dp)
    )
}

@Composable
private fun PharmLogoTitle() {
    Image(
        painter = painterResource(id = R.drawable.logo_image),
        contentDescription = "App Logo",
        modifier = Modifier.height(60.dp)
    )
}

@Composable
private fun PharmTextTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Primary
    )
}

@Composable
private fun PharmNavigationIcon(
    type: TopBarNavigationType,
    onClick: () -> Unit
) {
    val icon = when (type) {
        TopBarNavigationType.Back -> Icons.AutoMirrored.Filled.ArrowBack
        TopBarNavigationType.Menu -> Icons.Filled.Menu
        TopBarNavigationType.Close -> Icons.Filled.Close
        TopBarNavigationType.None -> return
    }

    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary
        )
    }
}

@Composable
private fun PharmActions(actions: List<TopBarAction>) {
    actions.forEach { action ->
        if (action.icon != null) {
            IconButton(onClick = action.onClick) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.text,
                    tint = Primary
                )
            }
        } else if (action.text != null) {
            TextButton(onClick = action.onClick) {
                Text(
                    text = action.text,
                    color = OnSurface
                )
            }
        }
    }
}