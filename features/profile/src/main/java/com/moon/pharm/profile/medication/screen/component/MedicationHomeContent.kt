package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.bar.PharmPrimaryTabRow
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.domain.model.medication.MedicationTimeGroup
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab

@Composable
fun MedicationHomeContent(
    selectedTab: MedicationPrimaryTab,
    currentList: List<MedicationTimeGroup>,
    totalCount: Int,
    completedCount: Int,
    onTabSelected: (MedicationPrimaryTab) -> Unit,
    onTakeClick: (TodayMedicationUiModel) -> Unit
) {
    val tabTitles = MedicationPrimaryTab.entries.map { it.title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        PharmPrimaryTabRow(
            selectedTabIndex = selectedTab.index,
            tabs = tabTitles,
            onTabSelected = { index -> onTabSelected(MedicationPrimaryTab.fromIndex(index)) }
        )

        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                MedicationProgressCard(total = totalCount, completed = completedCount)
            }

            items(items = currentList, key = { it.time ?: "no-time" }) { group ->
                MedicationGroupItem(group = group, onTakeClick = onTakeClick)
            }

            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}