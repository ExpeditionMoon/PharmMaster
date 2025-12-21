package com.moon.pharm.consult.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.view.PharmPrimaryTabRow
import com.moon.pharm.component_ui.view.StatusBadge
import com.moon.pharm.consult.mapper.toBackgroundColor
import com.moon.pharm.consult.mapper.toTextColor
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.domain.model.ConsultAnswer
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.ConsultStatus

@Composable
fun ConsultScreen(
    navController: NavController? = null,
    viewModel: ConsultViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    ConsultContent(
        selectedTab = uiState.selectedTab,
        currentList = if (uiState.selectedTab == ConsultPrimaryTab.LATEST) uiState.items else emptyList(),
        onTabSelected = { viewModel.onTabSelected(it) },
        onItemClick = { id ->
            navController?.navigate(ContentNavigationRoute.ConsultTabDetailScreen(id = id))
        }
    )

    if (uiState.isLoading) {
        // TODO: 로딩 화면
    }
}

@Composable
fun ConsultContent(
    selectedTab: ConsultPrimaryTab,
    currentList: List<ConsultItem>,
    onTabSelected: (ConsultPrimaryTab) -> Unit,
    onItemClick: (String) -> Unit
) {
    val tabTitles = ConsultPrimaryTab.entries.map { it.title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        PharmPrimaryTabRow(
            selectedTabIndex = selectedTab.index,
            tabs = tabTitles,
            onTabSelected = { index -> onTabSelected(ConsultPrimaryTab.fromIndex(index)) }
        )

        ConsultList(
            currentList = currentList,
            onItemClick = onItemClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ConsultList(
    currentList: List<ConsultItem>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
             .fillMaxSize()
             .background(backgroundLight)
    ) {
        items(
            items = currentList,
            key = { it.id }
        ) { item ->
            ConsultItemCard(
                item = item,
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

@Composable
fun ConsultItemCard(item: ConsultItem, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${item.userId} • ${item.createdAt}",
                    fontSize = 12.sp,
                    color = SecondFont
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            StatusBadge(
                text = item.status.label,
                statusColor = item.status.toBackgroundColor(),
                contentColor = item.status.toTextColor()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConsultContentPreview() {
    ConsultContent(
        selectedTab = ConsultPrimaryTab.LATEST,
        currentList = listOf(
            ConsultItem(
                id = "1",
                userId = "user01",
                expertId = null,
                title = "최근 복용 중인 영양제 관련 문의드립니다.",
                content = "오메가3랑 비타민D를 같이 먹어도 되나요?",
                status = ConsultStatus.WAITING,
                images = emptyList(),
                createdAt = "2025-06-17",
                answer = null
            ),
            ConsultItem(
                id = "2",
                userId = "user02",
                expertId = "expert_kim",
                title = "위장약 복용법 질문입니다.",
                content = "식전에 먹어야 하나요, 식후에 먹어야 하나요?",
                status = ConsultStatus.COMPLETED,
                images = emptyList(),
                createdAt = "2025-06-16",
                answer = ConsultAnswer(
                    answerId = "ans_01",
                    expertId = "expert_kim",
                    content = "식후 30분에 복용하시는 것을 권장합니다.",
                    createdAt = "2025-06-16"
                )
            )
        ),
        onTabSelected = {},
        onItemClick = { id -> println("클릭된 아이템 ID: $id") }
    )
}