package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moon.pharm.component_ui.component.bar.PharmPrimaryTabRow
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.domain.model.consult.ConsultItem

@Composable
fun ConsultContent(
    selectedTab: ConsultPrimaryTab,
    currentList: List<ConsultItem>,
    currentUserId: String?,
    isPharmacist: Boolean,
    onTabSelected: (ConsultPrimaryTab) -> Unit,
    onItemClick: (ConsultItem) -> Unit
) {
    val tabTitles = ConsultPrimaryTab.entries.map { it.title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PharmTheme.colors.background)
    ) {
        PharmPrimaryTabRow(
            selectedTabIndex = selectedTab.index,
            tabs = tabTitles,
            onTabSelected = { index -> onTabSelected(ConsultPrimaryTab.fromIndex(index)) })

        ConsultList(
            currentList = currentList,
            currentUserId = currentUserId,
            isPharmacist = isPharmacist,
            onItemClick = onItemClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@ThemePreviews
@Composable
private fun ConsultContentPreview() {
    PharmMasterTheme {
        ConsultContent(
            selectedTab = ConsultPrimaryTab.LATEST,
            currentList = ConsultPreviewData.dummyConsultItems,
            currentUserId = "u1",
            isPharmacist = false,
            onTabSelected = {},
            onItemClick = {}
        )
    }
}