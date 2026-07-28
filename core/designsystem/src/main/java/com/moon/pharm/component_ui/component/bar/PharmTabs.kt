package com.moon.pharm.component_ui.component.bar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

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
                color = PharmTheme.colors.primary
            )
        },
        containerColor = PharmTheme.colors.background,
        contentColor = PharmTheme.colors.primary,
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
                unselectedContentColor = PharmTheme.colors.secondFont
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PharmTabsPreview() {
    PharmMasterTheme {
        PharmPrimaryTabRow(
            selectedTabIndex = 0,
            tabs = listOf(R.string.common_confirm, R.string.common_cancel),
            onTabSelected = {}
        )
    }
}