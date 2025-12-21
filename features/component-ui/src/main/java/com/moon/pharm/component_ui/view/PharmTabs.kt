package com.moon.pharm.component_ui.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight

@Composable
fun PharmPrimaryTabRow(
    selectedTabIndex: Int,
    tabs: List<Int>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
                color = primaryLight
            )
        },
        containerColor = backgroundLight,
        contentColor = primaryLight,
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = stringResource(id = title),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                unselectedContentColor = SecondFont
            )
        }
    }
}
