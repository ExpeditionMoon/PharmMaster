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
import com.moon.pharm.designsystem.component.bar.PharmPrimaryTabRow
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.domain.usecase.medication.MedicationGroupIntakeItem
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.model.MedicationTimeGroupUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel

@Composable
fun MedicationHomeContent(
    selectedTab: MedicationPrimaryTab,
    currentList: List<MedicationTimeGroupUiModel>,
    totalCount: Int,
    completedCount: Int,
    weeklyTotalCount: Int,
    weeklyCompletedCount: Int,
    onTabSelected: (MedicationPrimaryTab) -> Unit,
    onTakeClick: (TodayMedicationUiModel) -> Unit,
    onEditClick: (String) -> Unit,
    onPauseClick: (String) -> Unit,
    onResumeClick: (String) -> Unit,
    onEndClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onCompleteGroup: (List<MedicationGroupIntakeItem>) -> Unit
) {
    val tabTitles = MedicationPrimaryTab.entries.map { it.title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PharmTheme.colors.background)
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
                MedicationProgressCard(
                    total = totalCount,
                    completed = completedCount,
                    weeklyTotal = weeklyTotalCount,
                    weeklyCompleted = weeklyCompletedCount
                )
            }

            items(items = currentList, key = MedicationTimeGroupUiModel::id) { group ->
                MedicationGroupItem(
                    group = group,
                    onTakeClick = onTakeClick,
                    onEditClick = onEditClick,
                    onPauseClick = onPauseClick,
                    onResumeClick = onResumeClick,
                    onEndClick = onEndClick,
                    onDeleteClick = onDeleteClick,
                    onCompleteGroup = onCompleteGroup
                )
            }

            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}

@ThemePreviews
@Composable
private fun MedicationHomeContentPreview() {
    PharmMasterTheme {
        MedicationHomeContent(
            selectedTab = MedicationPrimaryTab.ALL,
            currentList = emptyList(),
            totalCount = 3,
            completedCount = 1,
            weeklyTotalCount = 8,
            weeklyCompletedCount = 6,
            onTabSelected = {},
            onTakeClick = {},
            onEditClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onEndClick = {},
            onDeleteClick = {},
            onCompleteGroup = {}
        )
    }
}
